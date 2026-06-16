/**
 * 员工管理API
 * 对应后端 EmployeeController
 */

/**
 * 员工分页查询
 * @param {Object} params - 查询参数
 * @param {number} params.page - 页码
 * @param {number} params.pageSize - 每页大小
 * @param {string} [params.name] - 员工姓名（模糊搜索）
 * @returns {Promise} 分页结果
 */
function getMemberList(params) {
  return $axios({
    url: '/employee/page',
    method: 'get',
    params
  })
}

/**
 * 修改员工状态（启用/禁用）
 * @param {Object} params - 参数
 * @param {number} params.id - 员工ID
 * @param {number} params.status - 状态（0-禁用，1-启用）
 * @returns {Promise} 操作结果
 */
function enableOrDisableEmployee(params) {
  return $axios({
    url: '/employee',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 新增员工
 * @param {Object} params - 员工信息
 * @returns {Promise} 操作结果
 */
function addEmployee(params) {
  return $axios({
    url: '/employee',
    method: 'post',
    data: { ...params }
  })
}

/**
 * 修改员工信息
 * @param {Object} params - 员工信息
 * @returns {Promise} 操作结果
 */
function editEmployee(params) {
  return $axios({
    url: '/employee',
    method: 'put',
    data: { ...params }
  })
}

/**
 * 根据ID查询员工详情
 * @param {number} id - 员工ID
 * @returns {Promise} 员工详情
 */
function queryEmployeeById(id) {
  return $axios({
    url: `/employee/${id}`,
    method: 'get'
  })
}