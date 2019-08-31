<template>
  <div class="wrapper">
    <div class="announcement" style="background:white">
      <!-- 此处放公告 -->
      
    </div>
    <div class="sj-content">
      <div class="right-content">
        <div class="fl middle">
          <el-carousel :interval="4000" :height="bannerHeight+'px'">
            <el-carousel-item v-for="(item,index) in advertisementlist" :key="index">
              <a :href="item.url">
                <img ref="bannerHeight" :src="item.image" @load="imgLoad">
              </a>
            </el-carousel-item>
          </el-carousel>
          <div class="data-list">
            <ul class="headline fixed" id="headline">
              <li
                class="headline-item"
                v-for="(item,index) in hotlist"
                :key="index"
                @click="problemdetail(item.id)"
              >
                <div class="fl indexImg">
                  <img src>
                </div>
                <div class="words">
                  <h3>
                    {{item.title}}
                    <span>
                      <img src class="vip">
                    </span>
                  </h3>
                  <h5 class="author">
                    <div class="fl">
                      <span class="authorName">
                        <img :src="item.user.avatar" alt>
                        <a :href="'/user/persondata/'+item.user.id">{{item.user.nickname}}</a>
                      </span>
                      <span>{{formate(item.createtime)}}</span>
                    </div>
                    <div class="fr attention">
                      <span class="attentionText" style="color:#8A8A8A">评论数</span>
                      <span class="beforeclose">
                        <span>{{item.reply}}</span>
                      </span>
                    </div>
                    <div class="clearfix"></div>
                  </h5>
                </div>
                <p class="content" v-html="item.content"></p>
              </li>
            </ul>

            <ul id="loaded" class="headline"></ul>
          </div>
          <!-- <script src="~/assets/data-list.js" type="text/javascript"></script> -->
        </div>
        <div class="fl right">
          <div class="activity">
            <div class="acti">
              <a href="http://www.fafu.edu.cn/" target="_blank">
                <img src="~/assets/img/nav-right01.jpg">
              </a>
            </div>
            <div class="acti">
              <a href="http://jwgl.fafu.edu.cn" target="_blank">
                <img src="~/assets/img/nav-right02.jpg">
              </a>
            </div>
            <div class="acti">
              <a href="http://jxpt.fafu.edu.cn" target="_blank">
                <img src="~/assets/img/nav-right03.jpg">
              </a>
            </div>
            <div class="acti">
              <a href="http://app.fafu.edu.cn" target="_blank">
                <img src="~/assets/img/nav-right04.jpg">
              </a>
            </div>
          </div>

          <div class="activity">
            <div
              class="acti"
              v-for="(item,index) in gatheringlist"
              :key="index"
              @click="gatheringdetail(item.id)"
            >
              <img :src="item.image" :alt="item.summary">
            </div>
          </div>
          <div class="question-list">
            <h3 class="title" style="color:#409EFF">等待解决</h3>
            <div class="lists">
              <ul>
                <li
                  class="list-item"
                  v-for="(item,index) in waitlist"
                  :key="index"
                  @click="problemdetail(item.id)"
                >
                  <p class="list-title">{{item.title}}</p>
                  <p class="authorInfo">
                    <span class="authorName">
                      <img :src="item.user.avatar" alt>
                      <a :href="'/user/persondata/'+item.user.id">{{item.user.nickname}}</a>
                    </span>
                    <span>&nbsp;&nbsp;{{formate(item.createtime)}}</span>
                  </p>
                </li>
              </ul>
              <a href="/qa/label/0" class="sui-btn btn-block btn-bordered btn-more">查看更多</a>
            </div>
          </div>
          <div class="card-list"></div>

          <!-- <link rel="import" href=".~/assets/.~/assets/modules/ui-modules/footer/footer.html?__inline"> -->
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import problemApi from "@/api/problem";
import { formatDate } from "@/utils/date";
import gatheringApi from "@/api/gathering";
import advertisementApi from "@/api/advertisement";
import axios from "axios";
export default {
  data() {
    return {
      bannerHeight: ""
    };
  },
  asyncData() {
    return axios
      .all([
        problemApi.list("hotlist", "0", 1, 15),
        problemApi.list("waitlist", "0", 1, 5),
        gatheringApi.search(1, 3, { state: "1" }),
        advertisementApi.getAll()
      ])
      .then(
        axios.spread(function(
          hotlist,
          waitlist,
          gatheringlist,
          advertisementlist
        ) {
          return {
            hotlist: hotlist.data.data.rows,
            waitlist: waitlist.data.data.rows,
            gatheringlist: gatheringlist.data.data.rows,
            advertisementlist: advertisementlist.data.data
          };
        })
      );
  },
  methods: {
    formate(oridate) {
      return formatDate(oridate);
    },
    problemdetail(id) {
      this.$router.push("/qa/problem/" + id);
    },
    gatheringdetail(id) {
      this.$router.push("/gathering/item/" + id);
    },
    imgLoad() {
      this.$nextTick(() => {
        this.bannerHeight = this.$refs.bannerHeight[0].height;
      });
    }
  },
  mounted() {
    this.imgLoad();
    window.addEventListener(
      "resize",
      () => {
        this.bannerHeight = this.$refs.bannerHeight[0].height;
        this.imgLoad();
      },
      false
    );
  }
};
</script>


<style scoped>
@import "~/assets/css/page-headline-logined.css";
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>