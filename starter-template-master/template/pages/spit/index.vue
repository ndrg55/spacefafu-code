<template>
  <div>
    <div class="wrapper tag-item">
      <div class="fl left-list">
        <div class="tc-data-list">
          <div class="tc-list" v-infinite-scroll="loadMore">
            <ul class="detail-list">
              <li class="qa-item" v-for="(item,index) in items" :key="index">
                <div class="fl record">
                  <div class="number">
                    <div class="border useful">
                      <p class="usenum" @click="thumbup(index)">
                        <a class="zan">
                          <i :class="'fa fa-thumbs-up '+item.zan" aria-hidden="true"></i>
                        </a>
                      </p>
                      <p class="zannum">{{item.thumbup}}</p>
                    </div>
                    <div class="border answer">
                      <!-- <a href="#" class="star">
                        <i class="fa fa-star-o" aria-hidden="true"></i>
                      </a> -->
                    </div>
                  </div>
                </div>
                <div class="info">
                  <p class="text">
                    <router-link :to="'/spit/'+item._id" v-html="item.content"></router-link>
                  </p>
                  <div class="other">
                    <div class="fl date">
                      <img :src="item.user.avatar" width="30px" height="30px" alt>
                       <a :href="'/user/persondata/'+item.user.id"> {{item.user.nickname}}</a>
                      <span>{{formate(item.publishtime)}}&nbsp;发布</span>
                    </div>
                    <div class="fr remark">
                      <!-- <a href="#" data-toggle="modal" data-target="#shareModal" class="share">
                        <i class="fa fa-share-alt" aria-hidden="true"></i> 分享
                      </a> -->
                      <a href="#" data-toggle="modal" data-target="#remarkModal" class="comment">
                        <i class="fa fa-commenting" aria-hidden="true"></i> <router-link :to="'/spit/'+item._id" >回复</router-link>
                      </a>
                    </div>
                  </div>
                </div>
                <div class="clearfix"></div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="fl right-tag">
        <div class="block-btn">
          <p>有什么不满吗？说出来让自己好受点</p>
          <router-link class="sui-btn btn-block btn-share" to="/spit/submit">发吐槽</router-link>
        </div>
      </div>
      <div class="clearfix"></div>
    </div>
  </div>
</template>
<script>

import spitApi from "@/api/spit";
import { getUser } from "@/utils/auth";
import {formatDate} from "@/utils/date";
export default {
  asyncData() {
    return spitApi.search(1, 10).then(res => {
      let tmp = res.data.data.rows.map(item => {
        return {
          ...item,
          zan: ""
        };
      });

      return { items: tmp };
    });
  },
  data() {
    return {
      pageNo: 1
    };
  },
  methods: {
    loadMore() {
      this.pageNo++;
      spitApi.search(this.pageNo, 10).then(res => {
        let tmp = res.data.data.rows.map(item => {
          return {
            ...item,
            zan: ""
          };
        });
        this.items = this.items.concat(tmp);
      });
    },
    thumbup(index) {
      //判断用户是否登陆
      if (getUser().name === undefined) {
        this.$message({
          message: "必须登陆才可以点赞哦~",
          type: "warning"
        });
        return;
      }
      if (this.items[index].zan === "color") {
        this.$message({
          message: "不可以重复点赞哦~",
          type: "warning"
        });
        return;
      }

     
      spitApi.thumbup(this.items[index]._id,getUser().userid).then(res => {
        if (res.data.flag) {
          this.items[index].thumbup++;
        } else {
          this.$message({
            message: res.data.message,
            type: "warning"
          });
          return;
        }
      });
    },
    formate(oridate){
       return  formatDate(oridate);
    }
  }
};
</script>
<style scoped >
@import "~/assets/css/page-sj-spit-index.css";

</style>
