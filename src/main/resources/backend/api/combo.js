/**
 * 套餐管理API
 * 对应后端 SetmealController
 */

/**
 * 套餐分页查询
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页大小
 * @param {string} [params.name] - 套餐名称（模糊搜索）
 * @returns {Promise} 分页结果
 */
const getSetmealPage = (params) => {
  return $axios({
    url: '/setmeal/page',
    method: 'get',
    params
  })
}

/**
 * 删除套餐（支持批量）
 * @param {string|number} ids - 套餐ID，多个用逗号分隔
 * @returns {Promise} 操作结果
 */
const deleteSetmeal = (ids) => {
  return $axios({
    url: '/setmeal',
    method: 'delete',
    params: { ids }
  })
}

/**
 * 修改套餐
 * @param {Object} params - 套餐信息
 * @returns {Promise} 操作结果
 */
const editSetmeal = (params) => {
  return $axios({
    url: '/setmeal',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 新增套餐
 * @param {Object} params - 套餐信息
 * @returns {Promise} 操作结果
 */
const addSetmeal = (params) => {
  return $axios({
    url: '/setmeal',
    method: 'post',
    data: { ...params }
  })
}

/**
 * 根据ID查询套餐详情
 * @param {number} id - 套餐ID
 * @returns {Promise} 套餐详情
 */
const querySetmealById = (id) => {
  return $axios({
    url: `/setmeal/${id}`,
    method: 'get'
  })
}

/**
 * 批量修改套餐状态（起售/停售）
 * @param {Object} params - 参数
 * @param {string} params.ids - 套餐ID，多个用逗号分隔
 * @param {number} params.status - 状态（0-停售，1-启售）
 * @returns {Promise} 操作结果
 */
const setmealStatusByStatus = (params) => {
  return $axios({
    url: `/setmeal/status/${params.status}`,
    method: 'post',
    params: { ids: params.ids }
  })
}
