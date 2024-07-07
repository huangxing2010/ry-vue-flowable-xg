import request from '@/utils/request'

// 查询任务管理列表
export function listTask(query) {
  return request({
    url: '/oa/task/list',
    method: 'get',
    params: query
  })
}

//根据项目id和任务类型查询任务列表
export function getTaskByDictType(pid){
  return request({
    url: '/oa/task/getTaskByDictType/' + pid,
    method: 'get'
  })
}

// 根据工作类型查询列表
export function listTaskByType(query) {
  return request({
    url: '/oa/task/listByType',
    method: 'get',
    params: query
  })
}

// 查询任务管理详细
export function getTask(taskId) {
  return request({
    url: '/oa/task/' + taskId,
    method: 'get'
  })
}

// 新增任务管理
export function addTask(data) {
  return request({
    url: '/oa/task',
    method: 'post',
    data: data
  })
}

// 修改任务管理
export function updateTask(data) {
  return request({
    url: '/oa/task',
    method: 'put',
    data: data
  })
}

// 删除任务管理
export function delTask(taskId) {
  return request({
    url: '/oa/task/' + taskId,
    method: 'delete'
  })
}
