import request from '@/utils/request'
const group_name = 'chat'
const api_name = 'chatlist'
export default {
    search(data,page,size){
        return request({
            url:`/${group_name}/${api_name}/search/${page}/${size}`,
            method: 'post',
            data:data
        })
    }, 
    searchByType(keyword,page,size,type){
        return request({
            url:`/${group_name}/${api_name}/search/${keyword}/${page}/${size}/${type}`,
            method: 'get',
        })
    },
    updateunreadling(userid,touserid){
        return request({
            url:`/${group_name}/${api_name}/${userid}/${touserid}`,
            method: 'put',
        })
    },
    getUnReadNum(userid){
        return request({
            url:`/${group_name}/${api_name}/unread/${userid}`,
            method: 'get',
        })

    }
}