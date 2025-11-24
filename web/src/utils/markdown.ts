import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'
import katex from 'katex'
import 'katex/dist/katex.min.css'

// 创建markdown实例
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: (str, lang) => {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return `<pre class="hljs"><code>${hljs.highlight(str, { language: lang }).value}</code></pre>`
      } catch (e) {
        console.error(e)
      }
    }
    return `<pre class="hljs"><code>${md.utils.escapeHtml(str)}</code></pre>`
  },
})

// 自定义规则：支持LaTeX公式
const defaultRender = md.renderer.rules.text || ((tokens, idx) => tokens[idx].content)

md.renderer.rules.text = (tokens, idx, options, env, self) => {
  let content = tokens[idx].content

  // 行内公式 $...$
  content = content.replace(/\$([^\$]+)\$/g, (match, formula) => {
    try {
      return katex.renderToString(formula, { throwOnError: false })
    } catch (e) {
      console.error('LaTeX render error:', e)
      return match
    }
  })

  tokens[idx].content = content
  return defaultRender(tokens, idx, options, env, self)
}

// 块级公式 $$...$$
md.block.ruler.before('fence', 'katex_block', (state, startLine, endLine, silent) => {
  let pos = state.bMarks[startLine] + state.tShift[startLine]
  let max = state.eMarks[startLine]

  if (pos + 2 > max) return false
  if (state.src.slice(pos, pos + 2) !== '$$') return false

  pos += 2
  let firstLine = state.src.slice(pos, max)

  if (silent) return true
  if (firstLine.trim().endsWith('$$') && firstLine.trim() !== '$$') {
    firstLine = firstLine.trim().slice(0, -2)
  }

  let nextLine = startLine
  let lastLine = ''
  let found = false

  while (nextLine < endLine) {
    nextLine++
    if (nextLine >= endLine) break

    pos = state.bMarks[nextLine] + state.tShift[nextLine]
    max = state.eMarks[nextLine]
    const currentLine = state.src.slice(pos, max)

    if (currentLine.trim().endsWith('$$')) {
      lastLine = currentLine.trim().slice(0, -2)
      found = true
      break
    }
  }

  if (!found) return false

  const formula = [firstLine, lastLine].filter(Boolean).join('\n')

  const token = state.push('html_block', '', 0)
  try {
    token.content = katex.renderToString(formula, { displayMode: true, throwOnError: false })
  } catch (e) {
    console.error('LaTeX render error:', e)
    token.content = `<pre>$$${formula}$$</pre>`
  }

  state.line = nextLine + 1
  return true
})

// 渲染markdown
export const renderMarkdown = (content: string): string => {
  return md.render(content)
}

// 渲染LaTeX公式
export const renderLatex = (formula: string, displayMode = false): string => {
  try {
    return katex.renderToString(formula, { displayMode, throwOnError: false })
  } catch (e) {
    console.error('LaTeX render error:', e)
    return formula
  }
}

export default md

