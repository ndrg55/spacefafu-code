<template>
  <div>
    <el-container>
      <el-header>
        <div id="search">
          <el-select v-model="value" placeholder="请选择" style="float:left" @change="changesearch">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
          <div class="search bar6" style="float:left">
            <form>
              <input type="text" v-model="searchinfo" placeholder="请输入关键字.." name="cname">
              <button type="button" @click="searchall()"></button>
            </form>
          </div>
        </div>
      </el-header>
      <el-main>
        <div>
          <dl
            class="search-list J_search"
            data-track-click="{&quot;mod&quot;:&quot;popu_271&quot;,&quot;con&quot;:&quot;https://blog.csdn.net/dongzhou123/article/details/50834774&quot;}"
          >
            <div class="item" v-for="(item,index) in searchlist" :key="index">
              <dt>
                <div class="limit_width" @click="communioninfo(item.contentType,item.contentId)">
                  <a>
                    <em>{{item.title}}</em>
                  </a>
                  <!-- <a
                  
                  >&nbsp;-&nbsp;CSDN博客</a>-->
                </div>
                <span class="flag_icon flag_icon1">{{item.contentType=="1"?"互助":"吐槽"}}</span>
              </dt>

              <dd
                class="search-detail"
                @click="communioninfo(item.contentType,item.contentId)"
              >{{item.content}}</dd>

              <dd class="author-time">
                <span class="author">
                  作者：
                  <a :href="'/user/persondata/'+item.user.id">{{item.user.nickname}}</a>
                </span>
                
                <span class="date">日期：{{item.time.substring(0,10)}}</span>

                <!-- <span class="down fr">{{item.reply}}</span> -->
              </dd>
            </div>
            <div class="sui-pagination pages" style="float:right">
              <ul>
                <li class="prev">
                  <a href="javascript:void(0);" @click="upAndDown(-1)">上一页</a>
                </li>
                <li class="dotted" v-if="currentPage>2">
                  <span>...</span>
                </li>
                <li
                  :class="currentPage==count?'active':''"
                  v-for="count in pages"
                  :key="count"
                  @click="tiaozhuan(count)"
                >
                  <a
                    v-if="currentPage-count<2&&currentPage-count>-2"
                    href="javascript:void(0);"
                  >{{count}}</a>
                </li>
                <li class="dotted" v-if="pages-currentPage>1">
                  <span>...</span>
                </li>
                <li class="next">
                  <a href="javascript:void(0);" @click="upAndDown(1)">下一页</a>
                </li>
              </ul>
            </div>
          </dl>
        </div>
      </el-main>
    </el-container>
  </div>
</template>
<script>
import searchApi from "@/api/search";
export default {
  data() {
    return {
      searchinfo: "",
      searchlist: {},
      currentPage: 1,
      type: 2,
      pages: 1,
      size: 10,
      options: [
        {
          value: "2",
          label: "全部"
        },
        {
          value: "1",
          label: "互助"
        },
        {
          value: "0",
          label: "吐槽"
        }
      ],
      value: "2"
    };
  },
  created() {
    this.searchinfo = this.$route.params.searchinfo;
    searchApi.search(this.searchinfo, 1, this.size).then(res => {
      this.searchlist = res.data.data.rows;
      this.currentPage = 1;
      this.pages = Math.ceil(res.data.data.total / this.size);
    });
  },
  methods: {
    searchall() {
      if (this.searchinfo == "") {
        this.$message({
          showClose: true,
          message: "请输入关键字",
          type: "warning",
          duration: 1000
        });
        return;
      }

      if (this.type == "2") {
        searchApi.search(this.searchinfo, 1, this.size).then(res => {
          this.searchlist = res.data.data.rows;
          this.currentPage = 1;
          this.pages = Math.ceil(res.data.data.total / this.size);
        });
      } else {
        searchApi.searchByType(this.searchinfo, 1, this.size, this.type).then(res => {
          this.searchlist = res.data.data.rows;
          this.currentPage = 1;
          this.pages = Math.ceil(res.data.data.total / this.size);
        });
      }
    },
    communioninfo(type, id) {
      let myurl = "";
      if (type == "1") {
        //互助类型
        myurl = "/qa/problem/" + id;
      }
      if (type == "0") {
        //吐槽类型
        myurl = "/spit/" + id;
      }
      let routeData = this.$router.resolve({ path: myurl });

      window.open(routeData.href, "_blank");
    },
    changesearch(selVal) {
      if (this.searchinfo == "") {
        return;
      }
      this.type = selVal;
      if (selVal == "2") {
        searchApi.search(this.searchinfo, 1, this.size).then(res => {
          this.searchlist = res.data.data.rows;
          this.currentPage = 1;
          this.pages = Math.ceil(res.data.data.total / this.size);
        });
      } else {
        searchApi.searchByType(this.searchinfo, 1, this.size, selVal).then(res => {
          this.searchlist = res.data.data.rows;
          this.currentPage = 1;
          this.pages = Math.ceil(res.data.data.total / this.size);
        });
      }
    },
     tiaozhuan(page){
      this.currentPage=page;

      if (this.type == "2") {
        searchApi.search(this.searchinfo, this.currentPage, this.size).then(res => {
          this.searchlist = res.data.data.rows;

        });
      } else {
        searchApi.searchByType(this.searchinfo, this.currentPage, this.size, this.type).then(res => {
          this.searchlist = res.data.data.rows;
        });
      }
      
    } ,
    upAndDown(opera){
     
       var tempPage=this.currentPage+opera;

       if(tempPage>this.pages||tempPage<1){
         return ;
       }
       this.currentPage=tempPage;

       if (this.type == "2") {
        searchApi.search(this.searchinfo, this.currentPage, this.size).then(res => {
          this.searchlist = res.data.data.rows;

        });
      } else {
        searchApi.searchByType(this.searchinfo, this.currentPage, this.size, this.type).then(res => {
          this.searchlist = res.data.data.rows;
        });
      }
    }

  }
};
</script>



<style scoped>
/* 搜索列表 */
#search {
  margin-left: 230px;
}
.search-list {
  margin-left: 220px;
  margin-top: 0px;
  width: 820px;
  overflow: hidden;
  /* margin: 30px 20px 10px 20px;  */
  background: #ffffff;
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.05);
  padding: 20px 24px;
  box-sizing: border-box;
  border-bottom: 1px solid #0000000d;
  cursor: pointer;
}

.search-list dt {
  font-family: Arial,
    "Microsoft YaHei, SF Pro Display, Roboto, Noto, Arial, PingFang SC, Hiragino Sans GB, sans-serif";
  font-size: 16px;
  line-height: 20px;
  margin-bottom: 10px;
  font-weight: normal;
  overflow: hidden;
}
.search-list .item {
  padding-bottom: 10px;
  margin-bottom: 20px;
  border-bottom: solid 1.5px #f5f5f5;
}
.search-list dt div.limit_width {
  width: 710px;
  float: left;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.search-list dt a {
  color: #3d3d3d;
  font-size: 18px;
}
.search-list dt span.flag_icon {
  display: inline-block;
  width: 40px;
  height: 20px;
  background: #ffffff;
  text-align: center;
  line-height: 20px;
  font-size: 14px;
}
.search-list dt span.flag_icon span,
ul {
  font-family: "Microsoft YaHei", "SF Pro Display", Roboto, Noto, Arial,
    "PingFang SC", sans-serif;
}
.search-list dt span.flag_icon1 {
  color: #409EFF;
  border: 1px solid #2D8CF0;
}

.search-list .search-detail {
  font-size: 14px;
  color: #999999;
  margin-bottom: 5px;
  font-family: Arial, Microsoft YaHei, SF Pro Display, Roboto, Noto, Arial,
    PingFang SC, Hiragino Sans GB, sans-serif;
  line-height: 22px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  margin-left: 30px;
  padding: 0;
  margin-top: 10px;
}
.search-list .author-time {
  color: #666666;
  font-family: Arial,
    "Microsoft YaHei, SF Pro Display, Roboto, Noto, Arial, PingFang SC, Hiragino Sans GB, sans-serif";
  font-size: 14px;
  margin-bottom: 3px;

  margin: 0;
  padding: 0;
  margin-top: 10px;
}
.search-list .author-time .author a {
  display: inline-block;
  max-width: 120px;
  overflow: hidden;
  height: 20px;
  line-height: 20px;
  vertical-align: middle;
}

.search-list .author-time .author {
  margin-right: 16px;
}
.search-list .author-time .date {
  margin-right: 16px;
  float: right !important;
}
.search-list .author-time .reply {
  margin-right: 16px;
}
.search-list .author-time .link {
  margin-right: 24px;
  width: 380px;
  display: inline-block;
  vertical-align: top;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.search-list .author-time .link a {
  color: #75c348;
  font-size: 12px;
}
.fr {
  float: right !important;
}
/* 布局 */
.el-header {
  background-color: #f5f5f5;
  color: #333;
  text-align: center;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  color: #333;
}

.el-container {
  margin-top: 20px;
  margin-bottom: 40px;
}

/* .el-container:nth-child(5) .el-aside,
  .el-container:nth-child(6) .el-aside {
    line-height: 260px;
  }
  
  .el-container:nth-child(7) .el-aside {
    line-height: 320px;
  } */

/* 搜索框 */
div.search {
  padding: 0px 0;
}
form {
  position: relative;
  width: 500px;
  margin: 0 auto;
}
input,
button {
  border: none;
  outline: none;
  box-sizing: border-box;
}
input {
  width: 100%;
  height: 42px;
  padding-left: 13px;
}
button {
  height: 42px;
  width: 42px;
  cursor: pointer;
  position: absolute;
}
/*搜索框6*/
.bar6 {
  margin-left: 20px;
}
.bar6 input {
  border: 2px solid #409eff;
  border-radius: 5px;
  background: transparent;
  top: 0;
  right: 0;
}
.bar6 button {
  background: #409eff;
  border-radius: 0 5px 5px 0;
  width: 60px;
  top: 0;
  right: 0;
}
.bar6 button:before {
  content: "搜索";
  font-size: 13px;
  color: #f9f0da;
}
</style>
