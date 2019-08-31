import request from '@/utils/request'
const group_name = 'spit'
const api_name = 'spit'
export default {
    search(page, size) {
        return request({
          url: `/${group_name}/${api_name}/comment/0/${page}/${size}`,
          method: 'get',

        })
    },
    findById(id) {
        return request({
          url: `/${group_name}/${api_name}/${id}`,
          method: 'get'
        })
    },
    commentlist(id,page, size) {
        return request({
          url: `/${group_name}/${api_name}/comment/${id}/${page}/${size}`,
          method: 'get' 
        })
    },
    thumbup(id,userid){
        return request({
            url: `/${group_name}/${api_name}/thumbup/${id}/${userid}`,
            method: 'put'
        })
    },
    save(pojo) {
        return request({
          url: `/${group_name}/${api_name}`,
          method: 'post',
          data: pojo
        })
    },
    findMys(userid){
      return request({
          url:`/${group_name}/${api_name}/search/${userid}/1/10`,
          method: 'get',
      })
  }
}
