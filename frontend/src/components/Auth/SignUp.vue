<template>
  <div class="container">
    <h1>Register</h1>
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
      <Button :disabled="isCharging" label="Register"
              @click.prevent="register"/>
    </form>
    <Toast position="bottom-right" group="bc" class="toast-register">
      <template #message="slotProps">
       <div style="width: 100%">
         <h3>{{ slotProps.message.summary }}</h3>
         <p>{{ slotProps.message.detail }}</p>
         <Button size="small" label="Login" severity="success" @click="login"></Button>
       </div>
      </template>
    </Toast>
  </div>
</template>

<script setup>
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import {ref} from "vue";
import axios from "axios";
import {useToast} from "primevue";
import {useRouter} from "vue-router";

const data = ref({
  email: '',
  password: ''
})

const toast = useToast();
const errors = ref({});
const router = useRouter();

function resetData() {
  data.value = {
    email: '',
    password: ''
  };
  errors.value = {};
}

const isCharging = ref(false);

async function register() {
  try {
    isCharging.value = true;
    await axios.post('/auth/register', data.value);
    resetData();
    toast.add({ severity: 'success', summary: 'Account created', detail: 'You can try to login now :)', group: 'bc' });
  } catch (error) {
    errors.value = error.response.data;
  } finally {
    isCharging.value = false
  }
}

function login() {
  router.push('/app/login');
}
</script>
