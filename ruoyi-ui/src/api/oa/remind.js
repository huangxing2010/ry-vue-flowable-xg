import request from '@/utils/request'

// 查询事件提醒列表
export function listRemind(query) {
  return request({
    url: '/oa/remind/list',
    method: 'get',
    params: query
  })
}

//查询待办事件列表
export function getRemindList(query) {
  return request({
    url: '/oa/remind/getRemindList',
    method: 'get',
    params: query
  })
}

export function getRemindCount(query) {
  return request({
    url: '/oa/remind/getRemindCount',
    method: 'get',
    params: query
  })
}




// 查询事件提醒详细
export function getRemind(remindId) {
  return request({
    url: '/oa/remind/' + remindId,
    method: 'get'
  })
}

// 新增事件提醒
export function addRemind(data) {
  return request({
    url: '/oa/remind',
    method: 'post',
    data: data
  })
}

// 修改事件提醒
export function updateRemind(data) {
  return request({
    url: '/oa/remind',
    method: 'put',
    data: data
  })
}

// 删除事件提醒
export function delRemind(remindId) {
  return request({
    url: '/oa/remind/' + remindId,
    method: 'delete'
  })
}
