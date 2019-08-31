<template>
  <div class="answers">
    <h4>
      <span>我的提问</span>
    </h4>
    <ul class="answer-list">
      <li v-for="(item,index) in myspit" :key="index">
        <span class="fl good">
          <span class="num">{{item.comment}}</span> 回答
        </span>
        <span class="title">
          <!-- <a href="#">{{item.title}}</a> -->
          <router-link :to="'/spit/'+item._id" >{{delHtmlTag(item.content)}}</router-link>  
        </span>
        <span class="fr date">{{formate(item.publishtime)}}</span>
        <span class="clearfix"></span>
      </li>
    </ul>
  </div>
</template>
<script>

import userApi from "@/api/user";
import spitApi from "@/api/spit";
import { getUser, removeUser } from "@/utils/auth";
import { formatDate } from "@/utils/date";
export default {
  data() {
    return {
      pageNo: "",
      myspit:[]
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
    spitApi.findMys(getUser().userid).then(res => {
      this.myspit=res.data.data.rows 
    });
  },
  methods:{
    formate(oridate) {
      return formatDate(oridate);
    },
    delHtmlTag(str){  
        var title = str.replace(/<[^>]+>/g,"");//去掉所有的html标记
        if(title.length > 40) {
            title = title.substring(0,40)+"...";
        }
        return title;
    } 
  }
};
</script>

<style scoped>
@import "~/assets/css/page-sj-person-myanswer.css";
</style>
