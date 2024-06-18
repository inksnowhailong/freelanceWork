<template>
  <div style="height:100%;">
    <el-container style="height: 100%; border: 1px solid #eee">
      
      <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
       <Aside></Aside> 
      </el-aside>

      <el-container>
        <el-header style="text-align: right; font-size: 12px; border-bottom: 1px solid red; line-height:60px">
          <Header name="翟小雅"/>
        </el-header>

        <el-main>
          <router-view/>

        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'

import Aside from '@/components/Aside.vue'


export default {
  name: 'Manage',
  components:{
    Header,
    Aside
  },
}
</script>
<style scoped>
.el-main {
  text-align: left;
}
</style>