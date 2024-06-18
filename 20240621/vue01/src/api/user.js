import request from '@/utils/request';

export const getUserList = (params) => {
    return request({
        url: '/user/page',
        method: 'get',
        data: params,
    })
}