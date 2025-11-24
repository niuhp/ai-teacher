import { createApp } from 'vue'
import App from './App-simple.vue'

console.log('main.ts loaded')

const app = createApp(App)
app.mount('#app')

console.log('app mounted')

