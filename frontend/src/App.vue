<template>
  <header>
    <a href="/"><img alt="logo" src="/logo.png"></a>
    <Menubar :model="items"/>
    <Menubar v-if="!serverNotConnected" :model="actions" style="width: 30%"/>
  </header>
  <main>
    <RouterView/>
  </main>
</template>

<script setup>
import {RouterView} from "vue-router";
import Menubar from 'primevue/menubar';
import {computed, onMounted, ref} from "vue";
import axios from "axios";

const serverNotConnected = ref(true);

const isAuthenticated = computed(() => {
  return !_.isNil(localStorage.getItem('isLoggedIn'));
})

const actions = computed(() => [
  {
    icon: 'pi pi-plus',
    label: 'Register',
    url: '/app/register',
    visible: !isAuthenticated.value
  },
  {
    label: 'Log In',
    icon: 'pi pi-sign-in',
    url: '/app/login',
    visible: !isAuthenticated.value
  },
  {
    icon: 'pi pi-sign-out',
    label: 'Log Out',
    command: () => {
      logout();
    },
    visible: isAuthenticated.value
  }
]);

const items = computed(() => [
  {
    label: 'Home',
    icon: 'pi pi-home',
    url: '/'
  },
  {
    label: 'API',
    icon: 'pi pi-desktop',
    items: [
      {
        label: 'Movies',
        url: '/app/popular-movies'
      },
      {
        label: 'TV Shows',
        url: '/app/popular-series'
      },
    ]
  },
  {
    label: 'Export',
    icon: 'pi pi-file-excel',
    url: '/app/export-data',
    visible: isAuthenticated.value
  },
  {
    label: 'Favorites',
    icon: 'pi pi-heart',
    url: '/app/saves',
    visible: isAuthenticated.value
  }
]);

onMounted(async () => {
  try {
    await axios.get('/health');
    serverNotConnected.value = false;
  } catch (error) {
    console.error('Error connecting to backend:', error);
    localStorage.removeItem('isLoggedIn');
    serverNotConnected.value = true;
  }
})

async function logout() {
  try {
    const response = await axios.post('/auth/logout');
    localStorage.removeItem('isLoggedIn');
    window.location.reload();
  } catch (error) {
    console.error('Error trying to logout:', error);
  }
}
</script>

