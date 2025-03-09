<template>
  <div class="container">
    <h1>Export to Excel</h1>
<!--    <ProgressSpinner v-if="serverNotConnected" class="charging"/>-->
    <form class="export-form">
      <div class="field">
        <label for="username">File Name</label>
        <InputText id="username" v-model="data.filename" placeholder="excel" variant="filled"/>
        <span v-if="errors.filename" class="error-message" v-html="errors.filename"></span>
      </div>
      <div class="field">
        <label for="page">Page Number</label>
        <InputNumber v-model="page" showButtons buttonLayout="horizontal" style="width: 150px" :min="1" :max="99" @value-change="updatePage">
          <template #incrementbuttonicon>
            <span class="pi pi-plus" />
          </template>
          <template #decrementbuttonicon>
            <span class="pi pi-minus" />
          </template>
        </InputNumber>
      </div>
      <div class="field">
        <label for="username">Data</label>
        <Select v-model="selection" :options="selectionList" option-label="name" placeholder="Select Data"
                @change="getData"/>
        <span v-if="errors.data" class="error-message" v-html="errors.data"></span>
      </div>
      <div v-if="selection" class="field">
        <label for="username">Fields</label>
        <MultiSelect v-model="data.fields" :max-selected-labels="3" :options="multiSelectionList" filter
                     optionLabel="name"
                     placeholder="Select Fields to Export"/>
        <span v-if="errors.fields" class="error-message" v-html="errors.fields"></span>
      </div>

      <Button :disabled="isDownloading || data.fields.length <= 0" icon="pi pi-download" label="Download Excel file"
              @click.prevent="downloadExcel" />
    </form>
  </div>
</template>

<script setup>
import MultiSelect from 'primevue/multiselect';
import Select from 'primevue/select';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import {computed, onMounted, ref, watch} from "vue";
import axios from "axios";
import InputNumber from 'primevue/inputnumber';
import ProgressSpinner from "primevue/progressspinner";

const serverNotConnected = ref(false);

const page = ref(1);

const data = ref({
  filename: '',
  fields: [],
  data: []
})

const errors = ref({
  filename: '',
  fields: [],
  data: []
})

const movieMultiList = [
  {
    name: 'ID',
    field: 'id',
    type: 'number'
  },
  {
    name: 'Title',
    field: 'title',
    type: 'string'
  },
  {
    name: 'Language',
    field: 'original_language',
    type: 'string'
  },
  {
    name: 'Average Vote',
    field: 'vote_average',
    type: 'number'
  },
  {
    name: 'Vote Count',
    field: 'vote_count',
    type: 'number'
  },
  {
    name: 'Popularity',
    field: 'popularity',
    type: 'number'
  },
];

const seriesMultiList = [
  {
    name: 'ID',
    field: 'id',
    type: 'number'
  },
  {
    name: 'Name',
    field: 'name',
    type: 'string'
  },
  {
    name: 'Language',
    field: 'original_language',
    type: 'string'
  },
  {
    name: 'Average Vote',
    field: 'vote_average',
    type: 'number'
  },
  {
    name: 'Vote Count',
    field: 'vote_count',
    type: 'number'
  },
  {
    name: 'Popularity',
    field: 'popularity',
    type: 'number'
  },
];

const selectionList = [
  {
    name: 'Movies',
    data: 'movies'
  },
  {
    name: 'TV Shows',
    data: 'series'
  }
];

const isDownloading = ref(false);
const selection = ref();

watch(isDownloading, (newVal) => {
  if (newVal) {
    document.title = ' ...';
  } else {
    document.title = 'Movies';
  }
})

const multiSelectionList = computed(() => {
  if (selection.value.data === 'movies') {
    return movieMultiList;
  } else {
    return seriesMultiList;
  }
})

async function getData() {
  try {
    const info = await axios.get('/api/popular-' + selection.value.data + '?page=' + page.value);
    if(selection.value.data === 'movies') {
      data.value.data = info.data.popularMovies.results;
    } else {
      data.value.data = info.data.results;
    }
    data.value.fields = [];
  } catch (error) {
    console.error('Error fetching data:', error);
  }
}

function updatePage() {
  if(data.value.data.length > 0) {
    getData();
    console.log('updated')
  }
}

function resetData() {
  data.value = {
    filename: '',
    fields: [],
    data: []
  };
  errors.value = {};
  selection.value = null;
  page.value = 1;
}

async function downloadExcel() {
  try {
    isDownloading.value = true;

    const selectedFieldNames = data.value.fields.map(item => {
      return {'name': item.field, 'type': item.type}
    });
    let filename = data.value.filename || 'excel';

    console.log(data.value);

    const requestPayload = {
      filename,
      fields: selectedFieldNames,
      data: data.value.data
    };

    const response = await axios.post('/excel/download', requestPayload, {
      responseType: 'arraybuffer'
    });

    resetData();

    const blob = new Blob([response.data], {type: 'application/octet-stream'});
    const url = window.URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', filename.endsWith('.xlsx') ? filename : filename + '.xlsx');
    document.body.appendChild(link);
    link.click();
    link.remove();
    window.URL.revokeObjectURL(url);

  } catch (error) {
    const arrayBuffer = error.response.data;
    const decoder = new TextDecoder('utf-8');
    const text = decoder.decode(arrayBuffer);
    try {
      errors.value = JSON.parse(text);
    } catch (parseError) {
      console.error('Cannot parse errors to JSON:', parseError);
    }
  } finally {
    isDownloading.value = false
  }
}
</script>