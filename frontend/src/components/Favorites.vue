<template>
  <div class="container">
    <h1>Favorites</h1>
    <ProgressSpinner v-if="!favorites" class="charging"/>
   <div v-else-if="favorites.length > 0">
     <DataTable :value="favorites" stripedRows>
       <Column header="ID">
         <template #body="{ data, index }">
           <p>{{ index + 1 }}</p>
         </template>
       </Column>
       <Column header="Watched" field="watched" sortable>
         <template #body="slotProps">
           <i class="pi pi-check-circle" style="color: green" v-if="slotProps.data.watched"></i>
           <i class="pi pi-times-circle" style="color: red" v-else ></i>
         </template>
       </Column>
       <Column field="title" header="Title" sortable></Column>
       <Column header="Poster">
         <template #body="slotProps">
           <img :src="'https://image.tmdb.org/t/p/original/'+ slotProps.data.poster" alt="" style="max-width: 150px">
         </template>
       </Column>
       <Column field="finish_date" header="Finish Date" sortable>
         <template #body="slotProps">
           <p> {{ formatDate(slotProps.data.finish_date) }} </p>
         </template>
       </Column>
       <Column field="rating" header="Rating" sortable>
         <template #body="slotProps">
           <Rating v-model="slotProps.data.rating" readonly v-if="slotProps.data.watched" />
         </template>
       </Column>
       <Column field="impression" header="Impression"></Column>
       <Column header="Actions">
         <template #body="slotProps">
           <div style="display: flex; flex-direction: column; gap: 4px">
             <Button icon="pi pi-pencil" outlined rounded @click="editFavorite(slotProps.data)" v-if="!slotProps.data.watched"/>
             <Button icon="pi pi-trash" outlined rounded severity="danger" @click="deleteFavorite(slotProps.data.id)" />
           </div>
         </template>
       </Column>
     </DataTable>
   </div>
    <div v-else>
      <p><em>No favorites yet saved!</em></p>
    </div>
    <Dialog v-model:visible="visible" modal header="🎬 Finished?" :style="{ width: '25rem' }" class="update-movie" :closable="false">
      <span>When you finish, come back and fill in the information.</span>
      <div>
        <label for="rating">Rating</label>
        <Rating v-model="data.rating" />
        <span v-if="errors.rating" class="error-message" v-html="errors.rating"></span>
      </div>
      <div>
        <label for="impression">Impression</label>
        <Textarea v-model="data.impression" rows="5" cols="30" />
        <span v-if="errors.impression" class="error-message" v-html="errors.impression"></span>
      </div>
      <template #footer>
        <Button label="Cancel" text severity="secondary" @click="clearData" autofocus />
        <Button label="Save" outlined severity="secondary" @click="updateFavorite" autofocus />
      </template>
    </Dialog>

  </div>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import DataTable from 'primevue/datatable';
import {Button} from "primevue";
import ProgressSpinner from "primevue/progressspinner";
import Column from "primevue/column";
import {Dialog} from "primevue";
import Rating from 'primevue/rating';
import Textarea from 'primevue/textarea';


const favorites = ref();
const visible = ref(false);
const currentFavorite = ref();
const data = ref({
  rating: null,
  impression: null
});

function formatDate(dateString) {
  if (!dateString) return '';
  const dateObj = new Date(dateString);
  return dateObj.toLocaleDateString();
}

const errors = ref({
  rating: null,
  impression: null
});

onMounted(async () => {
  try {
    const response = await axios.get(`/favorites`);
    favorites.value = response.data;
  } catch (error) {
    console.error("Error fetching favorites:", error);
  }
});


async function deleteFavorite(id) {
  try {
    const response = await axios.delete('/favorites/' + id);
    location.reload();
  } catch (error) {
    console.error("Error fetching movie genres:", error);
  }
}

function editFavorite(item) {
  visible.value = true;
  currentFavorite.value = item;
}

async function updateFavorite() {
  try {
    const response = await axios.put('/favorites/' + currentFavorite.value.id, data.value);
    clearData();
    location.reload();
  } catch (error) {
    errors.value = error.response?.data;
  }
}

function clearData() {
  currentFavorite.value = null
  data.value = {
    rating: null,
    impression: null
  }
  errors.value = {
    rating: null,
    impression: null
  }
  visible.value = false;
}

</script>