<template>
    <div>
        <h1>这是一个主页</h1>
    </div>
</template>
<script>
export default {
    name:"Home",
    mounted(){
        console.log(process.env.VUE_APP_BASE_API);
    }
}
</script>

<style scoped>

</style>

