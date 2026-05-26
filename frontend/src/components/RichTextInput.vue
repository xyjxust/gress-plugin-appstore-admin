<template>
<RichTextEditor v-model="inner" :placeholder="placeholder" :min-height="minHeight" :disabled="disabled" />
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { RichTextEditor } from '@keqi.gress/plugin-ui'

interface Props {
  modelValue?: string
  placeholder?: string
  rows?: number
  minHeight?: number
  disabled?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  placeholder: '',
  rows: 4,
  minHeight: 160,
  disabled: false,
})

const emit = defineEmits<{
  (e: 'update:modelValue', v: string): void
}>()

const inner = computed({
  get: () => props.modelValue ?? '',
  set: (v: string) => emit('update:modelValue', v),
})
</script>

