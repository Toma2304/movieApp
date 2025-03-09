import {createApp} from 'vue'
import {createRouter, createWebHistory} from 'vue-router'
import App from './App.vue'
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';
import Home from "./components/Home.vue";
import PopularMovies from "./components/Movies/PopularMovies.vue";
import _ from 'lodash';
import './styles/style.scss';
import 'primeicons/primeicons.css';
import MovieDetails from "./components/Movies/MovieDetails.vue";
import Export from "./components/Export.vue";
import PopularSeries from "./components/Series/PopularSeries.vue";
import SeriesDetails from "./components/Series/SeriesDetails.vue";
import ToastService from 'primevue/toastservice';
import SignUp from "./components/Auth/SignUp.vue";
import Login from "./components/Auth/Login.vue";
import Toast from "primevue/toast";
import Favorites from "./components/Favorites.vue";

window._ = _;

const routes = [
    {path: '/', component: Home},
    {
        path: '/app/popular-movies',
        component: PopularMovies,
    },
    {
        path: '/app/popular-movies/:movie',
        component: MovieDetails,
        name: 'movieDetails'
    },
    {
        path: '/app/popular-series',
        component: PopularSeries,
    },
    {
        path: '/app/popular-series/:series',
        component: SeriesDetails,
        name: 'seriesDetails'
    },
    {
        path: '/app/export-data',
        component: Export,
        meta: { requiresAuth: true }
    },
    {
        path: '/app/saves',
        component: Favorites,
        meta: { requiresAuth: true }
    },
    {
        path: '/app/register',
        component: SignUp
    },
    {
        path: '/app/login',
        component: Login
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

function isAuthenticated() {
    return !_.isNil(localStorage.getItem('isLoggedIn'))
}

router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth && !isAuthenticated()) {
        next('/')
    } else {
        next()
    }
})


const app = createApp(App);
app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            darkModeSelector: false,
        }
    }
});
app.use(ToastService);
app.component('Toast', Toast);
app.use(router);
app.mount('#app')
