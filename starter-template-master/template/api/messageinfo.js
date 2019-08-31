import request from '@/utils/request'
const group_name = 'user'
const api_name = 'messageinfo'
export default {
    search(searchMap,page,size){
        return request({
            url:`/${group_name}/${api_name}/search/${page}/${size}`,
            method: 'post',
            data:searchMap
        })
    },
    deleteone(id,userid){
        return request({
            url:`/${group_name}/${api_name}/${id}/${userid}`,
            method: 'delete',
        })
    },
    deleteall(userid){
        return request({
            url:`/${group_name}/${api_name}/deleteall/${userid}`,
            method: 'delete',
        })
    }

}