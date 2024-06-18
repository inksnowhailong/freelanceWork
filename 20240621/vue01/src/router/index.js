import Vue from 'vue'
import VueRouter from 'vue-router'
import Manage from '../views/Manage.vue'
import User from '../views/User.vue'
import Login from '../views/Login.vue'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manage',
    redirect: '/login',
    component: Manage,
    children:[
      {
        path: 'user',
        name: 'User',
        component: User
      },
      {
        path: 'home',
        name: 'Home',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../views/Home.vue')
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import(/* webpackChunkName: "about" */ '../views/Order.vue')
      },
      {
        path: 'product',
        name: 'Product',
        component: () => import(/* webpackChunkName: "about" */ '../views/Product.vue')
      },
      {
        path: 'cart',
        name: 'Cart',
        component: () => import(/* webpackChunkName: "about" */ '../views/Cart.vue')
      },
      {
        path: 'warehouses',
        name: 'Warehouses',
        component: () => import(/* webpackChunkName: "about" */ '../views/Warehouses.vue')
      },

    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  }
]


   const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
   routes
  })

       export default router


