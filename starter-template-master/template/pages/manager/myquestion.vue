<template>
  <div class="answers">
    <h4>
      <span>我的提问</span>
    </h4>
    <ul class="answer-list">
      <li v-for="(item,index) in myanswers" :key="index">
        <span class="fl good">
          <span class="num">{{item.reply}}</span> 回答
        </span>
        <span class="title">
          <!-- <a href="#">{{item.title}}</a> -->
          <router-link :to="'/qa/problem/'+item.id" >{{item.title}}</router-link>  
        </span>
        <span class="fr date">{{formate(item.createtime)}}</span>
        <span class="clearfix"></span>
      </li>
    </ul>
  </div>
</template>
<script>

import userApi from "@/api/user";
import problemApi from "@/api/problem";
import { getUser, removeUser } from "@/utils/auth";
import { formatDate } from "@/utils/date";
export default {
  data() {
    return {
      pageNo: "",
      myanswers:[]
    };
  },
    beforeCreate(){
    //判断用户是否登陆
      if (getUser().name === undefined) {
        // this.$message({
        //   message: "请登陆",
        //   type: "warning"
        // });
        this.$router.push('/login')
        
      }
  },
  created() {
    problemApi.findMys(getUser().userid).then(res => {
      this.myanswers=res.data.data.rows 
    });
  },
  methods:{
    formate(oridate) {
      return formatDate(oridate);
    }
  }
};
</script>

<style scoped>
@import "~/assets/css/page-sj-person-myanswer.css";
</style>
