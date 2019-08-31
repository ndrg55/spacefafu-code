import request from '@/utils/request'
const group_name = 'user'
const api_name = 'messagenum'
export default {
    findbyuserid(id){
        return request({
            url:`/${group_name}/${api_name}/${id}`,
            method: 'get',
        })
    },
  

}