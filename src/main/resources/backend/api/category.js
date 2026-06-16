/**
 * 分类管理API
 * 对应后端 CategoryController
 */

/**
 * 分类分页查询
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页大小
 * @returns {Promise} 分页结果
 */
const getCategoryPage = (params) => {
  return $axios({
    url: '/category/page',
    method: 'get',
    params
  })
}

/**
 * 根据ID查询分类详情
 * @param {number} id - 分类ID
 * @returns {Promise} 分类详情
 */
const queryCategoryById = (id) => {
  return $axios({
    url: `/category/${id}`,
    method: 'get'
  })
}

/**
 * 删除分类
 * @param {number} id - 分类ID
 * @returns {Promise} 操作结果
 */
const deleteCategory = (id) => {
  return $axios({
    url: '/category',
    method: 'delete',
    params: { id }
  })
}

/**
 * 修改分类
 * @param {Object} params - 分类信息
 * @returns {Promise} 操作结果
 */
const editCategory = (params) => {
  return $axios({
    url: '/category',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 新增分类
 * @param {Object} params - 分类信息
 * @returns {Promise} 操作结果
 */
const addCategory = (params) => {
  return $axios({
    url: '/category',
    method: 'post',
    data: { ...params }
  })
}