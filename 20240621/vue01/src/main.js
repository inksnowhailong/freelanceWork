import Vue from 'vue'
import ElementUI from 'element-ui';// 添加
import 'element-ui/lib/theme-chalk/index.css'; // 添加
import App from './App.vue'
import router from './router'
import './assets/gloable.css' //添加
import request from './utils/request'

Vue.config.productionTip = false // 添加 取消生产提示
Vue.use(ElementUI);// 添加
Vue.prototype.request=request

new Vue({
 router,
 render: h => h(App)
}).$mount('#app')

