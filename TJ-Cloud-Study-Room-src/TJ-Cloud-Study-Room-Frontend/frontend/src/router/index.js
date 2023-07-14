import { createRouter, createWebHistory } from 'vue-router'
import AdminUserView from '../views/adminUserView.vue'
import AdminRoomView from "../views/adminRoomView.vue"
import Admin from "../views/admin.vue"
import Login from "../views/Login.vue"

import Register from "@/views/Register.vue";
import studyHall from "@/views/studyHall.vue";
import soloStudy from "@/views/soloStudy.vue";
import userCenter from "@/views/userCenter.vue";
import room from "@/views/Room.vue";

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/admin/user',
    name: 'User control',
    component: AdminUserView
  },
  {
    path: '/admin/room',
    name: 'Room control',
    component: AdminRoomView
  },
  {
    path: '/admin',
    name: 'Admin',
    component: Admin
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/studyHall',
    name: 'studyHall',
    component: studyHall
  },
  {
    path: '/userCenter',
    name: 'userCenter',
    component: userCenter
  },
  {
    path: '/soloStudy',
    name: 'soloStudy',
    component: soloStudy
  },
  {
    path: '/room',
    name: 'Room',
    component: room
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
