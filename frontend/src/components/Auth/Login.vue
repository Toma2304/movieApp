<template>
  <div class="container">
    <h1>Login</h1>
    <form class="export-form">
      <div class="field">
        <label for="email">Email</label>
        <InputText id="email" v-model="data.email" placeholder="john@gmail.com" variant="filled"/>
        <span v-if="errors.email" class="error-message" v-html="errors.email"></span>
      </div>
      <div class="field">
        <label for="password">Password</label>
        <InputText id="password" v-model="data.password" placeholder="********" variant="filled" type="password"/>
        <span v-if="errors.password" class="error-message" v-html="errors.password"></span>
      </div>
      <Button :disabled="isCharging" label="Login"
              @click.prevent="register"/>
    </form>
  </div>
</template>

<script setup>
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import CryptoJS from 'crypto-js';

const router = useRouter();
const data = ref({
  email: '',
  password: ''
})

const errors = ref({})

function resetData() {
  data.value = {
    email: '',
    password: ''
  };
  errors.value = {};
}

function toHome() {
  router.push('/').then(() => {
    window.location.reload();
  });
}

const isCharging = ref(false);

async function register(key, value) {
  try {
    isCharging.value = true;
    await axios.post('/auth/login', data.value);
    resetData();
    localStorage.setItem('isLoggedIn', CryptoJS.AES.encrypt('true', 'secret-key').toString());
    toHome();
  } catch (error) {
    console.log(error)
    errors.value = error.response?.data;
  } finally {
    isCharging.value = false
  }
}
</script>
