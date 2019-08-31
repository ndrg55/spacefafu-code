<template>
  <!--两列布局-->
  <div class="wrapper" style="margin-top:30px;margin-bottom:30px;">
    <div class="notice-time" style="padding:20px 10px 20px 40px">
      <h5 class="notice-title">通知提醒</h5>
      <div class="notice-content" v-infinite-scroll="loadMore">
        <div class="mark-all"></div>
        <div class="notice-day" style="margin-top:40px;" >
          <div class="everyday">
            <p class="date" style="margin-bottom:20px;">最新通知</p>
            <span class="btns">
                  <a
                    href="javascript:void(0);"
                    @click="open()"
                    class="sui-btn btn-bordered btn-danger btn-xlarge"
                  >全部标记为已读</a>
                </span>
            <ul class="operate"  > 
              <li style="margin-top:30px">
                
              </li>
              <li v-for="(item,index) in messageinfolist" :key="index">
                <span class="name">
                   <a :href="'/user/persondata/'+item.user.id"> {{item.user.nickname}}</a>
                </span>
                {{item.message}}
                <span class="red">
                  <a
                    @click="deleteone(item.id)"
                    :href="'/qa/problem/'+item.contentid"
                    v-if="(item.type==1)"
                  >{{item.title}}</a>
                  <a
                    @click="deleteone(item.id)"
                    :href="'/spit/'+item.contentid"
                    v-if="(item.type==2)"
                  >{{item.title}}</a>
                  <a @click="deleteone(item.id)" href="#" v-if="(item.type==3)">{{item.title}}</a>
                </span>
                <span class="btns">{{formate(item.time)}}</span>
              </li>

              
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { getUser, removeUser } from "@/utils/auth";
import userApi from "@/api/user";
import messagenumApi from "@/api/messagenum";
import messagenumInfoApi from "@/api/messageinfo";
import { formatDate } from "@/utils/date";
export default {
  data() {
    return {
      pageNo:1,
      messageinfolist: {}
    };
  },
  created() {
    messagenumInfoApi.search({ userid: getUser().userid },1,5).then(res => {

      this.messageinfolist = res.data.data.rows;
    });
  },
  methods: {
    loadMore(){
        this.pageNo++
        messagenumInfoApi.search({ userid: getUser().userid },this.pageNo,5).then( res => {
          this.messageinfolist = this.messageinfolist.concat( res.data.data.rows )
        })
      },
    formate(oridate) {
      return formatDate(oridate);
    },
    deleteone(id) {
      messagenumInfoApi.deleteone(id, getUser().userid);
    },
    deleteall() {
 
      messagenumInfoApi.deleteall(getUser().userid).then(res => {
        this.$message({
          message: res.data.message,
          type: res.data.flag ? "success" : "error"
        });
        location.reload();
      });
    },

    open() {
        this.$confirm('是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteall();
          this.$message({
            type: 'success',
            message: '操作成功!'
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消操作',
            duration: 1000,
          });          
        });
      }
  }
};
</script>

<style  scoped>
@import "~/assets/css/page-sj-other-notice.css";
</style>
