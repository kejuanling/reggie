/**
 * 菜品管理API
 * 对应后端 DishController
 */

/**
 * 菜品分页查询
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页大小
 * @param {string} [params.name] - 菜品名称（模糊搜索）
 * @returns {Promise} 分页结果
 */
const getDishPage = (params) => {
  return $axios({
    url: '/dish/page',
    method: 'get',
    params
  })
}

/**
 * 删除菜品（支持批量）
 * @param {string|number} ids - 菜品ID，多个用逗号分隔
 * @returns {Promise} 操作结果
 */
const deleteDish = (ids) => {
  return $axios({
    url: '/dish',
    method: 'delete',
    params: { ids }
  })
}

/**
 * 修改菜品
 * @param {Object} params - 菜品信息
 * @returns {Promise} 操作结果
 */
const editDish = (params) => {
  return $axios({
    url: '/dish',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 新增菜品
 * @param {Object} params - 菜品信息
 * @returns {Promise} 操作结果
 */
const addDish = (params) => {
  return $axios({
    url: '/dish',
    method: 'post',
    data: { ...params }
  })
}

/**
 * 根据ID查询菜品详情
 * @param {number} id - 菜品ID
 * @returns {Promise} 菜品详情
 */
const queryDishById = (id) => {
  return $axios({
    url: `/dish/${id}`,
    method: 'get'
  })
}

/**
 * 获取分类列表
 * @param {Object} params - 查询参数
 * @param {number} [params.type] - 分类类型（1-菜品，2-套餐）
 * @returns {Promise} 分类列表
 */
const getCategoryList = (params) => {
  return $axios({
    url: '/category/list',
    method: 'get',
    params
  })
}

/**
 * 查询菜品列表（不分页）
 * @param {Object} params - 查询参数
 * @param {number} [params.categoryId] - 分类ID
 * @returns {Promise} 菜品列表
 */
const queryDishList = (params) => {
  return $axios({
    url: '/dish/list',
    method: 'get',
    params
  })
}

/**
 * 文件下载（图片预览）
 * @param {Object} params - 查询参数
 * @param {string} params.name - 文件名
 * @returns {Promise} 文件流
 */
const commonDownload = (params) => {
  return $axios({
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
    },
    url: '/common/download',
    method: 'get',
    params
  })
}

/**
 * 批量修改菜品状态（起售/停售）
 * @param {Object} params - 参数
 * @param {string} params.id - 菜品ID，多个用逗号分隔
 * @param {number} params.status - 状态（0-停售，1-启售）
 * @returns {Promise} 操作结果
 */
const dishStatusByStatus = (params) => {
  return $axios({
    url: `/dish/status/${params.status}`,
    method: 'post',
    params: { ids: params.id }
  })
}