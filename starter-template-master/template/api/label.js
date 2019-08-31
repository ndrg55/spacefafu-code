import request from '@/utils/request'
const group_name = 'base'
const api_name = 'label'
export default {
    toplist(){
        return request({
            url: `/${group_name}/${api_name}`,
            method: 'get'
        })
    }

}