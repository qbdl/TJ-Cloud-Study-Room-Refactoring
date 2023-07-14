import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import '@/assets/css/global.css';
import dragDirective from './directive/drag';

const app = createApp(App);

// 注册自定义的拖拽指令
app.directive('drag', dragDirective);

app.use(store).use(router).use(ElementPlus).mount('#app');
