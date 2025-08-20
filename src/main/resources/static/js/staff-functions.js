// 工作人员页面功能扩展

// === 收费管理相关函数 ===
function getChargeManagementContent() {
    return `
        <div class="page-header">
            <h1 class="page-title">
                <i class="fas fa-credit-card me-3"></i>
                收费管理
            </h1>
            <p class="page-subtitle">管理各类收费项目和缴费记录</p>
        </div>
        <div class="dashboard-card">
            <div class="card-header">
                <h2 class="card-title">收费记录</h2>
                <div>
                    <button class="btn btn-primary me-2" onclick="goToAddCharge()">
                        <i class="fas fa-plus"></i>
                        新增收费记录
                    </button>
                    <button class="btn btn-success me-2" onclick="exportChargeData()">
                        <i class="fas fa-download"></i>
                        导出账单
                    </button>
                    <button class="btn btn-info" onclick="refreshChargeList()">
                        <i class="fas fa-refresh"></i>
                        刷新
                    </button>
                </div>
            </div>
            <div class="card-body">
                <div class="mb-3">
                    <div class="row">
                        <div class="col-md-3">
                            <select class="form-control" id="chargeStatusFilter" onchange="filterCharges()">
                                <option value="">全部状态</option>
                                <option value="UNPAID">未缴费</option>
                                <option value="PAID">已缴费</option>
                            </select>
                        </div>
                        <div class="col-md-9">
                            <input type="text" class="form-control" id="chargeSearchInput" placeholder="搜索业主姓名或收费项目..." onkeyup="searchCharges()">
                        </div>
                    </div>
                </div>
                <div class="table-container">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>收费项目</th>
                                <th>业主姓名</th>
                                <th>收费金额</th>
                                <th>收费说明</th>
                                <th>缴费状态</th>
                                <th>缴费时间</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody id="chargeTableBody">
                            <tr>
                                <td colspan="7" class="text-center">
                                    <div class="loading">
                                        <div class="spinner"></div>
                                        <span class="ms-2">加载收费信息中...</span>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>


    `;
}

function goToAddCharge() {
    window.location.href = 'staff-add-charge.html';
}

function refreshChargeList() {
    loadChargeList();
}

function exportChargeData() {
    alert('导出账单功能开发中...');
}

function filterCharges() {
    loadChargeList();
}

function searchCharges() {
    loadChargeList();
}

async function loadChargeList() {
    const tbody = document.getElementById('chargeTableBody');
    tbody.innerHTML = `
        <tr>
            <td colspan="7" class="text-center">
                <div class="loading">
                    <div class="spinner"></div>
                    <span class="ms-2">加载收费信息中...</span>
                </div>
            </td>
        </tr>
    `;

    try {
        const response = await fetch('/api/charges');
        if (response.ok) {
            const charges = await response.json();
            displayChargeList(charges);
        } else {
            throw new Error('获取收费信息失败');
        }
    } catch (error) {
        console.error('加载收费列表失败:', error);
        tbody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center text-danger">
                    <i class="fas fa-exclamation-triangle"></i>
                    加载收费信息失败，请稍后重试
                </td>
            </tr>
        `;
    }
}

function displayChargeList(charges) {
    const tbody = document.getElementById('chargeTableBody');
    if (charges.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="7" class="text-center text-muted">
                    <i class="fas fa-inbox"></i>
                    暂无收费记录
                </td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = charges.map(charge => `
        <tr>
            <td>${charge.chargeItem}</td>
            <td>${charge.owner ? (charge.owner.name || charge.owner.username) : '-'}</td>
            <td>¥${charge.amount.toFixed(2)}</td>
            <td>${charge.description || '-'}</td>
            <td>
                <span class="badge badge-${charge.paymentStatus === 'PAID' ? 'success' : 'warning'}">
                    ${charge.paymentStatus === 'PAID' ? '已缴费' : '未缴费'}
                </span>
            </td>
            <td>${charge.paymentTime ? new Date(charge.paymentTime).toLocaleDateString() : '-'}</td>
            <td>
                <button class="btn btn-sm btn-info me-1" onclick="editCharge(${charge.id})">
                    <i class="fas fa-edit"></i>
                    编辑
                </button>
                <button class="btn btn-sm btn-danger" onclick="deleteCharge(${charge.id})">
                    <i class="fas fa-trash"></i>
                    删除
                </button>
            </td>
        </tr>
    `).join('');
}

function editCharge(chargeId) {
    window.location.href = `staff-edit-charge.html?id=${chargeId}`;
}

async function deleteCharge(chargeId) {
    if (!confirm('确定要删除这条收费记录吗？此操作不可撤销。')) {
        return;
    }

    try {
        const response = await fetch(`/api/charges/${chargeId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('收费记录删除成功！');
            refreshChargeList();
        } else {
            alert('删除失败，请稍后重试');
        }
    } catch (error) {
        console.error('删除收费记录失败:', error);
        alert('删除失败，请稍后重试');
    }
}

// 扩展模块加载函数
if (typeof window.originalLoadModuleContent === 'undefined') {
    window.originalLoadModuleContent = window.loadModuleContent;
}

window.loadModuleContent = function(module) {
    const dynamicContent = document.getElementById('dynamic-content');
    let content = '';
    
    switch (module) {
        case 'charge-management':
            content = getChargeManagementContent();
            break;
        default:
            return window.originalLoadModuleContent(module);
    }
    
    dynamicContent.innerHTML = content;
    
    // 加载完内容后，根据模块类型加载数据
    setTimeout(() => {
        switch (module) {
            case 'charge-management':
                if (document.getElementById('chargeTableBody')) {
                    loadChargeList();
                } else {
                    console.warn('收费表格未找到，延迟重试');
                    setTimeout(() => {
                        if (document.getElementById('chargeTableBody')) {
                            loadChargeList();
                        }
                    }, 200);
                }
                break;
        }
    }, 100);
};
