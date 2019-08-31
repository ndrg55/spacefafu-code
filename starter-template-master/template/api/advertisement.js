import request from '@/utils/request'
const group_name = 'base'
const api_name = 'advertisement'
export default {
    getAll(){
        return request({
            url: `/${group_name}/${api_name}`,
            method: 'get'
        })
    }

}