import { Routes } from '@angular/router';
import { Products } from './products/products';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { AuthGuard } from './auth/auth-guard';

export const routes: Routes = [
    {
        path: '',
        redirectTo: 'products',
        pathMatch: 'full'
    },
    {
        path: 'products',
        canActivate: [AuthGuard],
        component: Products,
        data: { title: 'List of Products' }
    },
    {
        path: 'login',
        component: Login,
        data: { title: 'Login' }
    },
    {
        path: 'register',
        component: Register,
        data: { title: 'Register' }
    }
];
