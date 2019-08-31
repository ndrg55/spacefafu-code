<template>
  <div>
    <div class="banner"></div>
    <!--两列布局-->
    <div class="wrapper tag-item">
      <div class="fl left-list">
        <div class="friend-list">
          <h4>消息中心</h4>
          <ul>
            <li class="friend-item" v-for="(item,index) in chatlist" style="cursor: pointer;"  :key="index" @click="chatinfo(item.fromuserid)">
              <div class="tip">
                <span class="num" v-if="item.unread!=0">{{item.unread}}</span>
                <img :src="item.user.avatar" width="50px" height="50px" alt>
              </div>
              <div class="msg">
                <p>
                  <span class="name">{{item.user.nickname}}</span>
                  <span class="date">{{formate(item.lasttime)}}</span>
                </p>
                <p class="msg-content">{{item.message}}</p>
              </div>
            </li>
          </ul>
          <div class="sui-pagination pages">
            <ul>
              <li class="prev">
                <a href="javascript:void(0);" @click="upAndDown(-1)">上一页</a>
              </li>
              <li class="dotted" v-if="currentPage>2"><span>...</span></li> 
              <li  :class="currentPage==count?'active':''" v-for="count in pages" :key="count" @click="tiaozhuan(count)">
                <a  v-if="currentPage-count<2&&currentPage-count>-2" href="javascript:void(0);">{{count}}</a>
              </li>
              <li class="dotted" v-if="pages-currentPage>1"><span>...</span></li> 
              <li class="next">
                <a href="javascript:void(0);" @click="upAndDown(1)">下一页</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
      
      <div class="clearfix"></div>
    </div>
  </div>
</template>
<script>
import chatlistApi from "@/api/chatlist";
import { getUser } from "@/utils/auth";
import { formatDate } from "@/utils/date";
export default {
  data() {
    return {
      chatlist: {},
      currentPage:1,
      pages:0,
      size:10,
    };
  },
  create(){
    //判断用户是否登陆
      if (getUser().name === undefined) {
        this.$message({
          message: "请登陆",
          type: "warning"
        });
        this.$router.push('/login')
        return;
      }
  },
  mounted() {
    if(getUser().userid==undefined){
      location.href = "/login";
    }
    this.timer = -1; //首次设置小于0，立马调用getDatas函数
    this.intervalid2 = setInterval(() => {
      this.timer--;
      if (this.timer > 0) {
      } else {
        this.getDatas(); //调用getDatas函数
        this.timer = 10; //重置变量，60s后才能调用getDatas函数
      }
    }, 1000);
  },
  methods: {
    formate(oridate) {
      return formatDate(oridate);
    },
    getDatas() {
      chatlistApi.search({ userid: getUser().userid }, this.currentPage, this.size).then(res => {
        this.chatlist = res.data.data.rows;
      
        this.pages=Math.ceil(res.data.data.total/this.size );
      });
    },
    chatinfo(id){
     location.href = "/friendlist/chat/"+id;
    },
    tiaozhuan(page){
      this.currentPage=page;
      chatlistApi.search({ userid: getUser().userid }, this.currentPage, this.size).then(res => {
        this.chatlist = res.data.data.rows;
      });
      
    } ,
    upAndDown(opera){
     
       var tempPage=this.currentPage+opera;

       if(tempPage>this.pages||tempPage<1){
         return ;
       }
       this.currentPage=tempPage;
       chatlistApi.search({ userid: getUser().userid }, this.currentPage, this.size).then(res => {
        this.chatlist = res.data.data.rows;
      });
    }





  },
 
  beforeDestroy() {
    clearInterval(this.intervalid2);
  }
};
</script>
<style scoped>
@import "~/assets/css/page-sj-makeFriends-list.css";

</style>
