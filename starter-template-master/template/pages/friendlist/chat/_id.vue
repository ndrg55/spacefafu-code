<template>
  <!-- <div>
      昵称:<input v-model="name">
      <button @click="connect">登录聊天室</button>
      发送给：
      <input v-model="message.toUser">
      消息：<input v-model="message.message">
      <button @click="send">发送</button>
      接受到消息：
      <div v-html="info">       
      </div>
  </div>-->
  <div class="wrapper">
    <div style="padding:10px;">
      <h3>正在与&nbsp; <a :href="'/user/persondata/'+this.targetuser.id"> {{this.targetuser.nickname}}</a>&nbsp;聊天</h3>
    </div>
    <!-- 主容器 -->
    <div class="lite-chatbox dialog" id="data-list-content" style="padding:10px;">
      <div class="tips">
        <span class="tips-info" @click="more" >{{moretip}}</span>
    </div>
      <!-- -->
      <div :class="item.userid==targetuser.id?'cleft cmsg':'cright cmsg'" v-for="(item,index) in chainfolist" :key="index">
        <img
          class="headIcon radius"
          ondragstart="return false;"
          oncontextmenu="return false;"
          :src="item.userid==targetuser.id?targetuser.avatar:myavatar"
        >
        <span class="name">{{formate(item.createtime)}}</span>
        <span class="content">{{item.message}}</span>
      </div>

      
    </div>
    <div style="padding:10px 20px 0px 10px">
      <el-input type="textarea" resize="none"  :rows="4" placeholder="请输入内容" v-model="textarea"></el-input>
    </div>
    <el-row style="padding:5px 20px 3px 3px">
      <el-button type="primary" style="float:right" @click="send">发&nbsp;送</el-button>
    </el-row>
  </div>
</template>
<script>
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { getUser, removeUser } from "@/utils/auth";
import userApi from "@/api/user";
import chatinfoApi from "@/api/chatinfo";
import chatlistApi from "@/api/chatlist";
import { formatDate } from "@/utils/date";
export default {
  data() {
    return {
      name: "",
      stompClient: null,
      message: { toUser: this.$route.params.id, messageinfo: "" },
      info: "",
      textarea: "",
      targetuser:{},
      chainfolist:[],
      myavatar:getUser().avatar,
      moretip:"查看更多消息",
      flag:1,
      pageNo:1,
      chufagundongtiao:{flag:1}//触发滚动条到最下面的标记
    };
  },
  watch: {
    chufagundongtiao: {
      handler(newValue, oldValue) {
          this.scrollToBottom();
　　　　},
      deep:true
    }
  },
  create(){
   
  },
  created(){
     //判断用户是否登陆
      if (getUser().name === undefined) {
        // this.$message({
        //   message: "请登陆",
        //   type: "warning"
        // });
        this.$router.push('/login')
        
      }
    //ui初始化
    this.$nextTick(() => {
        var div = document.getElementById("data-list-content");

        div.scrollTop = div.scrollHeight;
      });
    //数据初始化
    userApi.info(this.$route.params.id).then(res=>{
      this.targetuser=res.data.data
    });
    chatinfoApi.getchat(this.$route.params.id,getUser().userid,1,4).then(res=>{
        this.chainfolist=res.data.data.rows.reverse();
        //触发滚动条到最下面
        this.chufagundongtiao.flag=this.chufagundongtiao.flag+1;
    });
    

    //unread为零
     chatlistApi.updateunreadling(getUser().userid,this.$route.params.id);
    //通信
    this.connect();
   
  },
  methods: {
    more(){
      if(this.flag==0){
        return ;
      }else{
        this.pageNo++;
        console.log("原来"+this.chainfolist.length);
        // 利用当前聊天数 当作每页的数量来分页查询 这样即时通讯的时候就不会错乱
          chatinfoApi.getchat(this.$route.params.id,getUser().userid,2,this.chainfolist.length).then(res=>{
              

              if(res.data.data.rows.length==0){
                this.moretip="没有更多消息..";
                this.flag=0;
              }else{
                //拼接 
                this.chainfolist=this.chainfolist.reverse().concat(res.data.data.rows).reverse();
              }
            //  this.chainfolist=res.data.data.rows.reverse();

            //   this.chainfolist = this.chainfolist.concat( res.data.data.rows )
          });
      }
    },
    formate(oridate) {
      return formatDate(oridate);
    },
    connect() {
      let socket = new SockJS("http://localhost:9020/my-websocket");
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({ login: getUser().userid }, this.onConnected);
    },
    onConnected(frame) {
      this.stompClient.subscribe("/user/topic/send", this.callback);
    },
    callback(msg) {
      var msgobject=JSON.parse(msg.body)
      //如果是当前窗口的对象发来的消息 更新当前窗口数据
      if(msgobject.userid===this.targetuser.id){
        this.chainfolist.push(msgobject);
        //unread为零
       chatlistApi.updateunreadling(getUser().userid,this.$route.params.id);
        //触发滚动条到最下面
        this.chufagundongtiao.flag=this.chufagundongtiao.flag+1;
      }else{
        console.log(msgobject)
        console.log(this.targetuser.id);
      }
       

      // this.chainfolist.push(msg.body)
    },
    send() {
      var myDate = new Date();
      myDate.toLocaleString();
      if(getUser().userid==this.targetuser.id){
        this.$message({
          message:"不能与自己聊天~",
          type:'error',
        });
        return ;
      }
      //先更新ui
      var tempmessageinfo={
        "userid":getUser().userid,
        "touserid":this.targetuser.id,
        "message":this.textarea,
        "createtime":this.formate(myDate),
      };
      this.chainfolist.push(tempmessageinfo);
      


      //往后台传
       var sendmessageinfo={
        "userid":getUser().userid,
        "touserid":this.targetuser.id,
        "message":this.textarea,
      };
      this.stompClient.send("/app/send", {}, JSON.stringify(sendmessageinfo));
      this.textarea="";
       //触发滚动条到最下面
        this.chufagundongtiao.flag=this.chufagundongtiao.flag+1;
    },
    sendtest(){
      this.message.toUser="111";
      // this.message.message="222";
    },
    scrollToBottom() {
      this.$nextTick(() => {
        var div = document.getElementById("data-list-content");

        div.scrollTop = div.scrollHeight;
      });
    }
  }
};
</script>
<style scoped>
@import "~/assets/css/litewebchat.min.css";

.wrapper {
  overflow: hidden;
  position: relative;
  margin: 10px auto;
  background-color: white;
}

@media (min-width: 1200px) {
  .wrapper {
    width: 970px;
    height: 550px;
  }
}

@media only screen and (min-width: 1024px) and (max-width: 1200px) {
  .wrapper {
    width: 800px;
    height: 510px;
  }
}

@media (max-width: 1024px) {
  .wrapper {
    width: 600px;
    height: 510px;
  }
}
.dialog {
  margin-left: auto;
  margin-right: auto;
  overflow-y: auto;
 
  background-color: #f9fcfd;
}

@media (min-width: 1200px) {
  .dialog {
     width: 900px;
     height: 320px;
  }
}

@media only screen and (min-width: 1024px) and (max-width: 1200px) {
  .dialog {
    width: 730px;
    height: 300px;
  }
}

@media (max-width: 1024px) {
  .dialog {
    width: 530px;
    height: 270px;
  }
}
</style>
