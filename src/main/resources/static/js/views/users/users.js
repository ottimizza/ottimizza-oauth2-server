import { findAllOrganizations, saveOrganization } from './../../services/organizations.service.js';
import { BootstrapBreadcrumbComponent, BootstrapPaginationComponent } from './../../components/bootstrap-breadcrumb.component.js';
import { fetchUsers } from '../../services/users.service.js';


Vue.component('suspension-points-animation', {
  template: `
    <div class="suspension-points">
      <div class="bounce1" title="Carregando..."></div>
      <div class="bounce2" title="Carregando..."></div>
      <div class="bounce3" title="Carregando..."></div>
    </div>
  `
});



const BREADCRUMB = [
  { label: 'Início', href: '/', active: false },
  { label: 'Usuários', href: '/usuarios', active: true },
]


// Tabela com Lista de organizações relacionadas ao Usuário logado.
let organizationsTable = new Vue({
  el: '#app',
  components: {
    'breadcrumb': BootstrapBreadcrumbComponent,
    'bootstrap-pagination': BootstrapPaginationComponent
  },
  data: {
    loading: false,
    breadcrumb: BREADCRUMB,
    pageInfo: {
      pageIndex: 0,
      pageSize: 10,
      totalPages: 0
    },
    organizations: []
  },
  methods: {
    findAllOrganizations: function (filter = '', pageIndex = this.pageInfo.pageIndex, pageSize = this.pageInfo.pageSize) {
      return fetchUsers(filter, pageIndex, pageSize);
    },
    onPageChange: function (event) {
      if (event.selected === 'first')
        this.pageInfo.pageIndex = 0;
      if (event.selected === 'previous')
        this.pageInfo.pageIndex = (this.pageInfo.pageIndex - 1 < 0) ? 0 : this.pageInfo.pageIndex - 1;
      if (event.selected === 'next')
        this.pageInfo.pageIndex = this.pageInfo.pageIndex + 1 > this.pageInfo.totalPages ? this.pageInfo.totalPages - 1 : this.pageInfo.pageIndex + 1;
      if (event.selected === 'last')
        this.pageInfo.pageIndex = this.pageInfo.totalPages - 1;
      this.loading = true;
      this.findAllOrganizations().then((response) => {
        this.loading = false;
        this.organizations = response.records;
        this.pageInfo = response.pageInfo;
      });
    }
  },
  created() {
    this.loading = true;
    this.findAllOrganizations().then((response) => {
      this.loading = false;
      this.organizations = response.records;
      this.pageInfo = response.pageInfo;
      console.log(this.pageInfo);

    });
  }
});

var organizationSidebarTab = new Vue({
  el: '#organization-create-tab',
  data: {
    loading: false,
    organization: { name: '', cnpj: '', codigoERP: '' }
  },
  methods: {
    findAllOrganizations: (filter = '', pageIndex = 0, pageSize = 10) => {
      return findAllOrganizations(filter, pageIndex, pageSize)
    },
    saveOrganization: async (organization = {}) => {
      if (organization) {
        if (typeof organization.name === 'undefined'
          || organization.name === null || organization.name === '') {
          return;
        }
        if (typeof organization.cnpj === 'undefined'
          || organization.cnpj === null || organization.cnpj === '') {
          return;
        }
        if (typeof organization.codigoERP === 'undefined'
          || organization.codigoERP === null || organization.codigoERP === '') {
          return;
        }
        return saveOrganization(organization).then((response) => {
          const organization = response;
          window.location = `/organizations/${organization.externalId}`;
        });;
      }
    }
  },
  created() { }
});


// Triggers
$('.organization-create-sidebar-cancel').on('click', function () {
  $('.notification-sidebar').removeClass('open');
});

