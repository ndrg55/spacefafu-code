<template>
  <div>
    <div class="tab-nav">
       <div class="wrapper">
        <ul class="fl sui-nav nav-tabs navbar-dark">
          <router-link tag="li" active-class="active" to="/qa/label/0" exact>
            <a>全部</a>
          </router-link>
          <router-link
            tag="li"
            active-class="active"
            :to="'/qa/label/'+label.id"
            v-for="(label,index) in labelList"
            :key="index"
          >
            <a>{{label.labelname}}</a>
          </router-link>
        </ul> 
      </div> 
          
    </div>
 
    <nuxt-child/>
  </div>
</template>
<script>
import labelApi from "@/api/label";

export default {
  asyncData() {
    return labelApi.toplist().then(res => {
      console.log(JSON.stringify(res.data.data));
      return { labelList: res.data.data };
    });
  },
  methods:{
      navchange(id){
        this.$router.push('/qa/label/'+id);
      }
  }
};
</script>
<style scoped>
@import "~/assets/css/page-sj-qa-logined.css";
</style>