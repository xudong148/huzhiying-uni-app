import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import AppLayout from '../layout/AppLayout.vue';
import DashboardView from '../views/DashboardView.vue';
import DispatchView from '../views/DispatchView.vue';
import OrdersView from '../views/OrdersView.vue';
import MastersView from '../views/MastersView.vue';
import PricingView from '../views/PricingView.vue';
import FinanceView from '../views/FinanceView.vue';
import ArbitrationView from '../views/ArbitrationView.vue';
import MarketingView from '../views/MarketingView.vue';
import ContentView from '../views/ContentView.vue';
import SystemView from '../views/SystemView.vue';

const routes = [
  {
    path: '/login',
    component: LoginView,
  },
  {
    path: '/',
    component: AppLayout,
    children: [
      { path: '', redirect: '/dashboard' },
      { path: '/dashboard', component: DashboardView, meta: { title: '仪表盘' } },
      { path: '/dispatch', component: DispatchView, meta: { title: '订单调度中心' } },
      { path: '/orders', component: OrdersView, meta: { title: '订单管理' } },
      { path: '/masters', component: MastersView, meta: { title: '师傅管理' } },
      { path: '/pricing', component: PricingView, meta: { title: '定价与类目' } },
      { path: '/finance', component: FinanceView, meta: { title: '财务结算' } },
      { path: '/arbitration', component: ArbitrationView, meta: { title: '仲裁中心' } },
      { path: '/marketing', component: MarketingView, meta: { title: '优惠券与会员' } },
      { path: '/content', component: ContentView, meta: { title: '内容运营' } },
      { path: '/system', component: SystemView, meta: { title: '系统权限' } },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
