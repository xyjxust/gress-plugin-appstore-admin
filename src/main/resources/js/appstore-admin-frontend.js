var __GRESS_PLUGIN__ = function(exports, vue, naiveUi) {
  "use strict";
  var __vite_style__ = document.createElement("style");
  __vite_style__.textContent = '@keyframes fadeIn {\n  from {\n    opacity: 0;\n  }\n  to {\n    opacity: 1;\n  }\n}\n@keyframes slideInUp {\n  from {\n    opacity: 0;\n    transform: translateY(20px);\n  }\n  to {\n    opacity: 1;\n    transform: translateY(0);\n  }\n}\n@keyframes scaleIn {\n  from {\n    opacity: 0;\n    transform: scale(0.9);\n  }\n  to {\n    opacity: 1;\n    transform: scale(1);\n  }\n}\n@keyframes pulse {\n  0%, 100% {\n    opacity: 1;\n  }\n  50% {\n    opacity: 0.5;\n  }\n}\n:root {\n  --primary: #6366f1;\n  --primary-light: #818cf8;\n  --primary-lighter: #a5b4fc;\n  --primary-lightest: #e0e7ff;\n  --primary-dark: #4f46e5;\n  --primary-darker: #4338ca;\n  --secondary: #8b5cf6;\n  --secondary-light: #a78bfa;\n  --secondary-lighter: #c4b5fd;\n  --secondary-lightest: #ede9fe;\n  --secondary-dark: #7c3aed;\n  --success: #10b981;\n  --success-light: #34d399;\n  --success-lighter: #6ee7b7;\n  --success-lightest: #d1fae5;\n  --success-dark: #059669;\n  --warning: #f59e0b;\n  --warning-light: #fbbf24;\n  --warning-lighter: #fcd34d;\n  --warning-lightest: #fef3c7;\n  --warning-dark: #d97706;\n  --error: #ef4444;\n  --error-light: #f87171;\n  --error-lighter: #fca5a5;\n  --error-lightest: #fee2e2;\n  --error-dark: #dc2626;\n  --info: #3b82f6;\n  --info-light: #60a5fa;\n  --info-lighter: #93c5fd;\n  --info-lightest: #dbeafe;\n  --info-dark: #2563eb;\n  --gray-50: #fafafa;\n  --gray-100: #f5f5f5;\n  --gray-200: #e5e5e5;\n  --gray-300: #d4d4d4;\n  --gray-400: #a3a3a3;\n  --gray-500: #737373;\n  --gray-600: #525252;\n  --gray-700: #404040;\n  --gray-800: #262626;\n  --gray-900: #171717;\n  --text-primary: #1f2329;\n  --text-secondary: #4e5969;\n  --text-tertiary: #86909c;\n  --text-quaternary: #c9cdd4;\n  --text-disabled: #e5e6eb;\n  --text-white: #ffffff;\n  --bg-primary: #ffffff;\n  --bg-secondary: #f7f8fa;\n  --bg-tertiary: #f2f3f5;\n  --bg-quaternary: #e5e6eb;\n  --bg-overlay: rgba(0, 0, 0, 0.6);\n  --bg-mask: rgba(0, 0, 0, 0.4);\n  --border-primary: #e5e6eb;\n  --border-secondary: #f0f2f5;\n  --border-tertiary: #dcdfe6;\n  --border-focus: #6366f1;\n  --spacing-xs: 4px;\n  --spacing-sm: 8px;\n  --spacing-md: 12px;\n  --spacing-lg: 16px;\n  --spacing-xl: 20px;\n  --spacing-2xl: 24px;\n  --spacing-3xl: 32px;\n  --spacing-4xl: 40px;\n  --spacing-5xl: 48px;\n  --spacing-6xl: 64px;\n  --radius-xs: 4px;\n  --radius-sm: 6px;\n  --radius-md: 8px;\n  --radius-lg: 12px;\n  --radius-xl: 16px;\n  --radius-2xl: 20px;\n  --radius-3xl: 24px;\n  --radius-full: 9999px;\n  --shadow-xs: 0 1px 2px rgba(0, 0, 0, 0.05);\n  --shadow-sm: 0 2px 4px rgba(0, 0, 0, 0.06);\n  --shadow-md: 0 4px 8px rgba(0, 0, 0, 0.08);\n  --shadow-lg: 0 8px 16px rgba(0, 0, 0, 0.1);\n  --shadow-xl: 0 12px 24px rgba(0, 0, 0, 0.12);\n  --shadow-2xl: 0 20px 40px rgba(0, 0, 0, 0.14);\n  --shadow-3xl: 0 24px 48px rgba(0, 0, 0, 0.16);\n  --shadow-primary: 0 4px 12px rgba(99, 102, 241, 0.25);\n  --shadow-primary-lg: 0 8px 20px rgba(99, 102, 241, 0.3);\n  --shadow-success: 0 4px 12px rgba(16, 185, 129, 0.25);\n  --shadow-warning: 0 4px 12px rgba(245, 158, 11, 0.25);\n  --shadow-error: 0 4px 12px rgba(239, 68, 68, 0.25);\n  --font-family-base: -apple-system, BlinkMacSystemFont, Segoe UI, Roboto, Helvetica Neue, Arial, Noto Sans, sans-serif, Apple Color Emoji, Segoe UI Emoji, Segoe UI Symbol, Noto Color Emoji;\n  --font-family-mono: JetBrains Mono, ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, Liberation Mono, Courier New, monospace;\n  --font-size-xs: 11px;\n  --font-size-sm: 12px;\n  --font-size-base: 13px;\n  --font-size-md: 14px;\n  --font-size-lg: 16px;\n  --font-size-xl: 18px;\n  --font-size-2xl: 20px;\n  --font-size-3xl: 24px;\n  --font-size-4xl: 28px;\n  --font-size-5xl: 32px;\n  --font-weight-light: 300;\n  --font-weight-normal: 400;\n  --font-weight-medium: 500;\n  --font-weight-semibold: 600;\n  --font-weight-bold: 700;\n  --line-height-tight: 1.2;\n  --line-height-normal: 1.5;\n  --line-height-relaxed: 1.6;\n  --line-height-loose: 1.8;\n  --transition-fast: 0.15s ease;\n  --transition-base: 0.2s ease;\n  --transition-slow: 0.3s ease;\n  --transition-slower: 0.4s ease;\n  --z-index-dropdown: 1000;\n  --z-index-sticky: 1020;\n  --z-index-fixed: 1030;\n  --z-index-modal-backdrop: 1040;\n  --z-index-modal: 1050;\n  --z-index-popover: 1060;\n  --z-index-tooltip: 1070;\n  --z-index-notification: 1080;\n  --button-height-sm: 28px;\n  --button-height-md: 32px;\n  --button-height-lg: 40px;\n  --input-height-sm: 28px;\n  --input-height-md: 32px;\n  --input-height-lg: 40px;\n  --card-padding-sm: 12px;\n  --card-padding-md: 16px;\n  --card-padding-lg: 24px;\n  --card-padding-xl: 32px;\n  --node-width: 260px;\n  --node-height: 140px;\n  --node-gap: 32px;\n  --node-border-width: 2px;\n}\n@keyframes fadeIn {\n  from {\n    opacity: 0;\n  }\n  to {\n    opacity: 1;\n  }\n}\n@keyframes slideInUp {\n  from {\n    opacity: 0;\n    transform: translateY(20px);\n  }\n  to {\n    opacity: 1;\n    transform: translateY(0);\n  }\n}\n@keyframes scaleIn {\n  from {\n    opacity: 0;\n    transform: scale(0.9);\n  }\n  to {\n    opacity: 1;\n    transform: scale(1);\n  }\n}\n@keyframes pulse {\n  0%, 100% {\n    opacity: 1;\n  }\n  50% {\n    opacity: 0.5;\n  }\n}\n@keyframes fadeIn {\n  from {\n    opacity: 0;\n  }\n  to {\n    opacity: 1;\n  }\n}\n@keyframes slideInUp {\n  from {\n    opacity: 0;\n    transform: translateY(20px);\n  }\n  to {\n    opacity: 1;\n    transform: translateY(0);\n  }\n}\n@keyframes scaleIn {\n  from {\n    opacity: 0;\n    transform: scale(0.9);\n  }\n  to {\n    opacity: 1;\n    transform: scale(1);\n  }\n}\n@keyframes pulse {\n  0%, 100% {\n    opacity: 1;\n  }\n  50% {\n    opacity: 0.5;\n  }\n}\n.m-0 {\n  margin: 0 !important;\n}\n.m-xs {\n  margin: 4px !important;\n}\n.m-sm {\n  margin: 8px !important;\n}\n.m-md {\n  margin: 12px !important;\n}\n.m-lg {\n  margin: 16px !important;\n}\n.m-xl {\n  margin: 20px !important;\n}\n.m-2xl {\n  margin: 24px !important;\n}\n.m-3xl {\n  margin: 32px !important;\n}\n.mt-0 {\n  margin-top: 0 !important;\n}\n.mt-xs {\n  margin-top: 4px !important;\n}\n.mt-sm {\n  margin-top: 8px !important;\n}\n.mt-md {\n  margin-top: 12px !important;\n}\n.mt-lg {\n  margin-top: 16px !important;\n}\n.mt-xl {\n  margin-top: 20px !important;\n}\n.mt-2xl {\n  margin-top: 24px !important;\n}\n.mt-3xl {\n  margin-top: 32px !important;\n}\n.mb-0 {\n  margin-bottom: 0 !important;\n}\n.mb-xs {\n  margin-bottom: 4px !important;\n}\n.mb-sm {\n  margin-bottom: 8px !important;\n}\n.mb-md {\n  margin-bottom: 12px !important;\n}\n.mb-lg {\n  margin-bottom: 16px !important;\n}\n.mb-xl {\n  margin-bottom: 20px !important;\n}\n.mb-2xl {\n  margin-bottom: 24px !important;\n}\n.mb-3xl {\n  margin-bottom: 32px !important;\n}\n.ml-0 {\n  margin-left: 0 !important;\n}\n.ml-xs {\n  margin-left: 4px !important;\n}\n.ml-sm {\n  margin-left: 8px !important;\n}\n.ml-md {\n  margin-left: 12px !important;\n}\n.ml-lg {\n  margin-left: 16px !important;\n}\n.ml-xl {\n  margin-left: 20px !important;\n}\n.ml-2xl {\n  margin-left: 24px !important;\n}\n.ml-3xl {\n  margin-left: 32px !important;\n}\n.mr-0 {\n  margin-right: 0 !important;\n}\n.mr-xs {\n  margin-right: 4px !important;\n}\n.mr-sm {\n  margin-right: 8px !important;\n}\n.mr-md {\n  margin-right: 12px !important;\n}\n.mr-lg {\n  margin-right: 16px !important;\n}\n.mr-xl {\n  margin-right: 20px !important;\n}\n.mr-2xl {\n  margin-right: 24px !important;\n}\n.mr-3xl {\n  margin-right: 32px !important;\n}\n.p-0 {\n  padding: 0 !important;\n}\n.p-xs {\n  padding: 4px !important;\n}\n.p-sm {\n  padding: 8px !important;\n}\n.p-md {\n  padding: 12px !important;\n}\n.p-lg {\n  padding: 16px !important;\n}\n.p-xl {\n  padding: 20px !important;\n}\n.p-2xl {\n  padding: 24px !important;\n}\n.p-3xl {\n  padding: 32px !important;\n}\n.pt-0 {\n  padding-top: 0 !important;\n}\n.pt-xs {\n  padding-top: 4px !important;\n}\n.pt-sm {\n  padding-top: 8px !important;\n}\n.pt-md {\n  padding-top: 12px !important;\n}\n.pt-lg {\n  padding-top: 16px !important;\n}\n.pt-xl {\n  padding-top: 20px !important;\n}\n.pt-2xl {\n  padding-top: 24px !important;\n}\n.pt-3xl {\n  padding-top: 32px !important;\n}\n.pb-0 {\n  padding-bottom: 0 !important;\n}\n.pb-xs {\n  padding-bottom: 4px !important;\n}\n.pb-sm {\n  padding-bottom: 8px !important;\n}\n.pb-md {\n  padding-bottom: 12px !important;\n}\n.pb-lg {\n  padding-bottom: 16px !important;\n}\n.pb-xl {\n  padding-bottom: 20px !important;\n}\n.pb-2xl {\n  padding-bottom: 24px !important;\n}\n.pb-3xl {\n  padding-bottom: 32px !important;\n}\n.text-left {\n  text-align: left !important;\n}\n.text-center {\n  text-align: center !important;\n}\n.text-right {\n  text-align: right !important;\n}\n.text-justify {\n  text-align: justify !important;\n}\n.text-primary {\n  color: #1f2329 !important;\n}\n.text-secondary {\n  color: #4e5969 !important;\n}\n.text-tertiary {\n  color: #86909c !important;\n}\n.text-white {\n  color: #ffffff !important;\n}\n.text-success {\n  color: #10b981 !important;\n}\n.text-warning {\n  color: #f59e0b !important;\n}\n.text-error {\n  color: #ef4444 !important;\n}\n.text-info {\n  color: #3b82f6 !important;\n}\n.text-xs {\n  font-size: 11px !important;\n}\n.text-sm {\n  font-size: 12px !important;\n}\n.text-base {\n  font-size: 13px !important;\n}\n.text-md {\n  font-size: 14px !important;\n}\n.text-lg {\n  font-size: 16px !important;\n}\n.text-xl {\n  font-size: 18px !important;\n}\n.text-2xl {\n  font-size: 20px !important;\n}\n.text-3xl {\n  font-size: 24px !important;\n}\n.font-light {\n  font-weight: 300 !important;\n}\n.font-normal {\n  font-weight: 400 !important;\n}\n.font-medium {\n  font-weight: 500 !important;\n}\n.font-semibold {\n  font-weight: 600 !important;\n}\n.font-bold {\n  font-weight: 700 !important;\n}\n.text-ellipsis {\n  overflow: hidden;\n  text-overflow: ellipsis;\n  white-space: nowrap;\n}\n.text-no-select {\n  user-select: none;\n  -webkit-user-select: none;\n  -moz-user-select: none;\n  -ms-user-select: none;\n}\n.d-none {\n  display: none !important;\n}\n.d-block {\n  display: block !important;\n}\n.d-inline {\n  display: inline !important;\n}\n.d-inline-block {\n  display: inline-block !important;\n}\n.d-flex {\n  display: flex !important;\n}\n.d-inline-flex {\n  display: inline-flex !important;\n}\n.d-grid {\n  display: grid !important;\n}\n.flex-row {\n  flex-direction: row !important;\n}\n.flex-column {\n  flex-direction: column !important;\n}\n.flex-wrap {\n  flex-wrap: wrap !important;\n}\n.flex-nowrap {\n  flex-wrap: nowrap !important;\n}\n.flex-1 {\n  flex: 1 !important;\n}\n.flex-auto {\n  flex: auto !important;\n}\n.flex-none {\n  flex: none !important;\n}\n.align-start {\n  align-items: flex-start !important;\n}\n.align-center {\n  align-items: center !important;\n}\n.align-end {\n  align-items: flex-end !important;\n}\n.align-stretch {\n  align-items: stretch !important;\n}\n.justify-start {\n  justify-content: flex-start !important;\n}\n.justify-center {\n  justify-content: center !important;\n}\n.justify-end {\n  justify-content: flex-end !important;\n}\n.justify-between {\n  justify-content: space-between !important;\n}\n.justify-around {\n  justify-content: space-around !important;\n}\n.gap-xs {\n  gap: 4px !important;\n}\n.gap-sm {\n  gap: 8px !important;\n}\n.gap-md {\n  gap: 12px !important;\n}\n.gap-lg {\n  gap: 16px !important;\n}\n.gap-xl {\n  gap: 20px !important;\n}\n.gap-2xl {\n  gap: 24px !important;\n}\n.position-relative {\n  position: relative !important;\n}\n.position-absolute {\n  position: absolute !important;\n}\n.position-fixed {\n  position: fixed !important;\n}\n.position-sticky {\n  position: sticky !important;\n}\n.w-full {\n  width: 100% !important;\n}\n.w-auto {\n  width: auto !important;\n}\n.w-screen {\n  width: 100vw !important;\n}\n.h-full {\n  height: 100% !important;\n}\n.h-auto {\n  height: auto !important;\n}\n.h-screen {\n  height: 100vh !important;\n}\n.min-w-0 {\n  min-width: 0 !important;\n}\n.max-w-full {\n  max-width: 100% !important;\n}\n.min-h-0 {\n  min-height: 0 !important;\n}\n.max-h-full {\n  max-height: 100% !important;\n}\n.bg-primary {\n  background-color: #ffffff !important;\n}\n.bg-secondary {\n  background-color: #f7f8fa !important;\n}\n.bg-tertiary {\n  background-color: #f2f3f5 !important;\n}\n.bg-transparent {\n  background-color: transparent !important;\n}\n.bg-gradient-primary {\n  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);\n}\n.bg-gradient-success {\n  background: linear-gradient(135deg, #10b981 0%, #14b8a6 100%);\n}\n.bg-gradient-warning {\n  background: linear-gradient(135deg, #f59e0b 0%, #fb923c 100%);\n}\n.bg-gradient-error {\n  background: linear-gradient(135deg, #ef4444 0%, #f43f5e 100%);\n}\n.border {\n  border: 1px solid #e5e6eb !important;\n}\n.border-0 {\n  border: none !important;\n}\n.border-t {\n  border-top: 1px solid #e5e6eb !important;\n}\n.border-b {\n  border-bottom: 1px solid #e5e6eb !important;\n}\n.border-l {\n  border-left: 1px solid #e5e6eb !important;\n}\n.border-r {\n  border-right: 1px solid #e5e6eb !important;\n}\n.rounded-none {\n  border-radius: 0 !important;\n}\n.rounded-xs {\n  border-radius: 4px !important;\n}\n.rounded-sm {\n  border-radius: 6px !important;\n}\n.rounded-md {\n  border-radius: 8px !important;\n}\n.rounded-lg {\n  border-radius: 12px !important;\n}\n.rounded-xl {\n  border-radius: 16px !important;\n}\n.rounded-2xl {\n  border-radius: 20px !important;\n}\n.rounded-full {\n  border-radius: 9999px !important;\n}\n.shadow-none {\n  box-shadow: none !important;\n}\n.shadow-xs {\n  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05) !important;\n}\n.shadow-sm {\n  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.06) !important;\n}\n.shadow-md {\n  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08) !important;\n}\n.shadow-lg {\n  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1) !important;\n}\n.shadow-xl {\n  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12) !important;\n}\n.shadow-2xl {\n  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.14) !important;\n}\n.shadow-primary {\n  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.25) !important;\n}\n.overflow-hidden {\n  overflow: hidden !important;\n}\n.overflow-auto {\n  overflow: auto !important;\n}\n.overflow-scroll {\n  overflow: scroll !important;\n}\n.overflow-x-auto {\n  overflow-x: auto !important;\n}\n.overflow-y-auto {\n  overflow-y: auto !important;\n}\n.cursor-pointer {\n  cursor: pointer !important;\n}\n.cursor-default {\n  cursor: default !important;\n}\n.cursor-not-allowed {\n  cursor: not-allowed !important;\n}\n.opacity-0 {\n  opacity: 0 !important;\n}\n.opacity-25 {\n  opacity: 0.25 !important;\n}\n.opacity-50 {\n  opacity: 0.5 !important;\n}\n.opacity-75 {\n  opacity: 0.75 !important;\n}\n.opacity-100 {\n  opacity: 1 !important;\n}\n.z-0 {\n  z-index: 0 !important;\n}\n.z-10 {\n  z-index: 10 !important;\n}\n.z-20 {\n  z-index: 20 !important;\n}\n.z-30 {\n  z-index: 30 !important;\n}\n.z-40 {\n  z-index: 40 !important;\n}\n.z-50 {\n  z-index: 50 !important;\n}\n.pointer-events-none {\n  pointer-events: none !important;\n}\n.pointer-events-auto {\n  pointer-events: auto !important;\n}\n.transition-fast {\n  transition: all 0.15s ease !important;\n}\n.transition-base {\n  transition: all 0.2s ease !important;\n}\n.transition-slow {\n  transition: all 0.3s ease !important;\n}\n@keyframes fadeIn {\n  from {\n    opacity: 0;\n  }\n  to {\n    opacity: 1;\n  }\n}\n@keyframes slideInUp {\n  from {\n    opacity: 0;\n    transform: translateY(20px);\n  }\n  to {\n    opacity: 1;\n    transform: translateY(0);\n  }\n}\n@keyframes scaleIn {\n  from {\n    opacity: 0;\n    transform: scale(0.9);\n  }\n  to {\n    opacity: 1;\n    transform: scale(1);\n  }\n}\n@keyframes pulse {\n  0%, 100% {\n    opacity: 1;\n  }\n  50% {\n    opacity: 0.5;\n  }\n}\n.gress-button {\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  gap: 8px;\n  border: none;\n  border-radius: 8px;\n  font-weight: 500;\n  cursor: pointer;\n  transition: all 0.2s ease;\n  white-space: nowrap;\n  user-select: none;\n  -webkit-user-select: none;\n  -moz-user-select: none;\n  -ms-user-select: none;\n}\n.gress-button:disabled {\n  opacity: 0.5;\n  cursor: not-allowed;\n}\n.gress-button {\n  height: 32px;\n  padding: 0 16px;\n  font-size: 13px;\n}\n.gress-button--primary {\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  gap: 8px;\n  border: none;\n  border-radius: 8px;\n  font-weight: 500;\n  cursor: pointer;\n  transition: all 0.2s ease;\n  white-space: nowrap;\n  user-select: none;\n  -webkit-user-select: none;\n  -moz-user-select: none;\n  -ms-user-select: none;\n}\n.gress-button--primary:disabled {\n  opacity: 0.5;\n  cursor: not-allowed;\n}\n.gress-button--primary {\n  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);\n  color: #ffffff;\n  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.25);\n}\n.gress-button--primary:hover:not(:disabled) {\n  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);\n  transform: translateY(-1px);\n}\n.gress-button--primary:active:not(:disabled) {\n  transform: translateY(0);\n}\n.gress-button--secondary {\n  background: #f7f8fa;\n  color: #1f2329;\n  border: 1px solid #e5e6eb;\n}\n.gress-button--secondary:hover:not(:disabled) {\n  background: #f2f3f5;\n  border-color: #dcdfe6;\n}\n.gress-button--success {\n  background: linear-gradient(135deg, #10b981 0%, #14b8a6 100%);\n  color: #ffffff;\n  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.25);\n}\n.gress-button--success:hover:not(:disabled) {\n  box-shadow: 0 8px 20px rgba(16, 185, 129, 0.3);\n  transform: translateY(-1px);\n}\n.gress-button--warning {\n  background: linear-gradient(135deg, #f59e0b 0%, #fb923c 100%);\n  color: #ffffff;\n  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.25);\n}\n.gress-button--warning:hover:not(:disabled) {\n  box-shadow: 0 8px 20px rgba(245, 158, 11, 0.3);\n  transform: translateY(-1px);\n}\n.gress-button--error {\n  background: linear-gradient(135deg, #ef4444 0%, #f43f5e 100%);\n  color: #ffffff;\n  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.25);\n}\n.gress-button--error:hover:not(:disabled) {\n  box-shadow: 0 8px 20px rgba(239, 68, 68, 0.3);\n  transform: translateY(-1px);\n}\n.gress-button--text {\n  background: transparent;\n  color: #6366f1;\n}\n.gress-button--text:hover:not(:disabled) {\n  background: rgba(99, 102, 241, 0.08);\n}\n.gress-button--sm {\n  height: 28px;\n  padding: 0 12px;\n  font-size: 12px;\n}\n.gress-button--lg {\n  height: 40px;\n  padding: 0 20px;\n  font-size: 14px;\n}\n.gress-button--block {\n  width: 100%;\n}\n.gress-button--icon {\n  width: 32px;\n  padding: 0;\n}\n.gress-button--icon.gress-button--sm {\n  width: 28px;\n}\n.gress-button--icon.gress-button--lg {\n  width: 40px;\n}\n.gress-card {\n  background: #ffffff;\n  border-radius: 16px;\n  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);\n  padding: 16px;\n  border: 1px solid #f0f2f5;\n}\n.gress-card--hover {\n  transition: all 0.2s ease;\n}\n.gress-card--hover:hover {\n  transform: translateY(-2px);\n  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);\n}\n.gress-card--sm {\n  padding: 12px;\n}\n.gress-card--lg {\n  padding: 24px;\n}\n.gress-card--xl {\n  padding: 32px;\n}\n.gress-card--gradient {\n  background: linear-gradient(135deg, #f8f9ff 0%, #ffffff 100%);\n  border: 1px solid rgba(99, 102, 241, 0.1);\n}\n.gress-card__header {\n  display: flex;\n  align-items: center;\n  justify-content: space-between;\n  margin-bottom: 16px;\n  padding-bottom: 16px;\n  border-bottom: 1px solid #f0f2f5;\n}\n.gress-card__title {\n  font-size: 16px;\n  font-weight: 600;\n  color: #1f2329;\n}\n.gress-card__subtitle {\n  font-size: 12px;\n  color: #86909c;\n  margin-top: 4px;\n}\n.gress-card__body {\n  flex: 1;\n}\n.gress-card__footer {\n  display: flex;\n  align-items: center;\n  justify-content: space-between;\n  margin-top: 16px;\n  padding-top: 16px;\n  border-top: 1px solid #f0f2f5;\n}\n.gress-input {\n  width: 100%;\n  border: 1px solid #e5e6eb;\n  border-radius: 8px;\n  background: #ffffff;\n  color: #1f2329;\n  font-size: 13px;\n  transition: all 0.2s ease;\n}\n.gress-input:hover {\n  border-color: #dcdfe6;\n}\n.gress-input:focus {\n  outline: none;\n  border-color: #6366f1;\n  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);\n}\n.gress-input:disabled {\n  background: #f2f3f5;\n  color: #e5e6eb;\n  cursor: not-allowed;\n}\n.gress-input::placeholder {\n  color: #86909c;\n}\n.gress-input {\n  height: 32px;\n  padding: 0 12px;\n}\n.gress-input--sm {\n  height: 28px;\n  padding: 0 8px;\n  font-size: 12px;\n}\n.gress-input--lg {\n  height: 40px;\n  padding: 0 16px;\n  font-size: 14px;\n}\n.gress-input--error {\n  border-color: #ef4444;\n}\n.gress-input--error:focus {\n  border-color: #ef4444;\n  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);\n}\n.gress-input--success {\n  border-color: #10b981;\n}\n.gress-input--success:focus {\n  border-color: #10b981;\n  box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);\n}\n.gress-badge {\n  display: inline-flex;\n  align-items: center;\n  padding: 2px 8px;\n  border-radius: 6px;\n  background: #6366f1;\n  color: #ffffff;\n  font-size: 11px;\n  font-weight: 500;\n  line-height: 1.2;\n}\n.gress-badge--primary {\n  background: rgba(99, 102, 241, 0.1);\n  color: #6366f1;\n}\n.gress-badge--success {\n  background: rgba(16, 185, 129, 0.1);\n  color: #10b981;\n}\n.gress-badge--warning {\n  background: rgba(245, 158, 11, 0.1);\n  color: #f59e0b;\n}\n.gress-badge--error {\n  background: rgba(239, 68, 68, 0.1);\n  color: #ef4444;\n}\n.gress-badge--info {\n  background: rgba(59, 130, 246, 0.1);\n  color: #3b82f6;\n}\n.gress-badge--gradient {\n  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);\n  color: #ffffff;\n}\n.gress-tabs__nav {\n  display: flex;\n  align-items: center;\n  gap: 8px;\n  border-bottom: 2px solid #f0f2f5;\n  margin-bottom: 24px;\n}\n.gress-tabs__item {\n  background: none;\n  border: none;\n  padding: 0;\n  margin: 0;\n  font: inherit;\n  color: inherit;\n  cursor: pointer;\n  outline: none;\n  padding: 12px 16px;\n  font-size: 13px;\n  font-weight: 500;\n  color: #4e5969;\n  border-bottom: 2px solid transparent;\n  margin-bottom: -2px;\n  transition: all 0.2s ease;\n}\n.gress-tabs__item:hover {\n  color: #6366f1;\n}\n.gress-tabs__item--active {\n  color: #6366f1;\n  border-bottom-color: #6366f1;\n}\n.gress-modal__overlay {\n  position: absolute;\n  top: 0;\n  left: 0;\n  right: 0;\n  bottom: 0;\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  background: rgba(0, 0, 0, 0.6);\n  z-index: 1040;\n  animation: fadeIn 0.2s ease;\n}\n.gress-modal__container {\n  background: #ffffff;\n  border-radius: 20px;\n  box-shadow: 0 24px 48px rgba(0, 0, 0, 0.16);\n  padding: 32px;\n  border: 1px solid #f0f2f5;\n  max-width: 90vw;\n  max-height: 90vh;\n  overflow: auto;\n  animation: scaleIn 0.2s ease;\n}\n.gress-modal__header {\n  display: flex;\n  align-items: center;\n  justify-content: space-between;\n  margin-bottom: 24px;\n}\n.gress-modal__title {\n  font-size: 20px;\n  font-weight: 600;\n  color: #1f2329;\n}\n.gress-modal__close {\n  background: none;\n  border: none;\n  padding: 0;\n  margin: 0;\n  font: inherit;\n  color: inherit;\n  cursor: pointer;\n  outline: none;\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  width: 32px;\n  height: 32px;\n  border-radius: 9999px;\n  color: #86909c;\n  transition: all 0.2s ease;\n}\n.gress-modal__close:hover {\n  background: #f7f8fa;\n  color: #1f2329;\n}\n.gress-modal__body {\n  margin-bottom: 24px;\n}\n.gress-modal__footer {\n  display: flex;\n  align-items: center;\n  justify-content: space-between;\n  gap: 12px;\n}\n.gress-dropdown {\n  position: relative;\n}\n.gress-dropdown__menu {\n  background: #ffffff;\n  border-radius: 12px;\n  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);\n  padding: 8px;\n  border: 1px solid #f0f2f5;\n  position: absolute;\n  top: 100%;\n  left: 0;\n  margin-top: 8px;\n  min-width: 160px;\n  z-index: 1000;\n  animation: slideInUp 0.2s ease;\n}\n.gress-dropdown__item {\n  background: none;\n  border: none;\n  padding: 0;\n  margin: 0;\n  font: inherit;\n  color: inherit;\n  cursor: pointer;\n  outline: none;\n  display: flex;\n  align-items: center;\n  width: 100%;\n  padding: 12px;\n  gap: 12px;\n  font-size: 13px;\n  color: #1f2329;\n  border-radius: 8px;\n  transition: all 0.2s ease;\n  text-align: left;\n}\n.gress-dropdown__item:hover {\n  background: #f7f8fa;\n}\n.gress-dropdown__item--active {\n  background: rgba(99, 102, 241, 0.1);\n  color: #6366f1;\n}\n.gress-dropdown__item--danger {\n  color: #ef4444;\n}\n.gress-dropdown__item--danger:hover {\n  background: rgba(239, 68, 68, 0.1);\n}\n.gress-dropdown__divider {\n  height: 1px;\n  background: #f0f2f5;\n  margin: 8px 0;\n}\n.gress-notification {\n  background: #ffffff;\n  border-radius: 16px;\n  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.12);\n  padding: 16px;\n  border: 1px solid #f0f2f5;\n  display: flex;\n  align-items: center;\n  gap: 12px;\n  min-width: 320px;\n  max-width: 480px;\n  animation: slideInUp 0.2s ease;\n}\n.gress-notification__icon {\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  width: 40px;\n  height: 40px;\n  border-radius: 9999px;\n  flex-shrink: 0;\n}\n.gress-notification__content {\n  flex: 1;\n}\n.gress-notification__title {\n  font-size: 14px;\n  font-weight: 600;\n  color: #1f2329;\n  margin-bottom: 4px;\n}\n.gress-notification__message {\n  font-size: 12px;\n  color: #4e5969;\n  line-height: 1.6;\n}\n.gress-notification__close {\n  background: none;\n  border: none;\n  padding: 0;\n  margin: 0;\n  font: inherit;\n  color: inherit;\n  cursor: pointer;\n  outline: none;\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  width: 24px;\n  height: 24px;\n  border-radius: 9999px;\n  color: #86909c;\n  flex-shrink: 0;\n  transition: all 0.2s ease;\n}\n.gress-notification__close:hover {\n  background: #f7f8fa;\n  color: #1f2329;\n}\n.gress-notification--success .gress-notification__icon {\n  background: rgba(16, 185, 129, 0.1);\n  color: #10b981;\n}\n.gress-notification--warning .gress-notification__icon {\n  background: rgba(245, 158, 11, 0.1);\n  color: #f59e0b;\n}\n.gress-notification--error .gress-notification__icon {\n  background: rgba(239, 68, 68, 0.1);\n  color: #ef4444;\n}\n.gress-notification--info .gress-notification__icon {\n  background: rgba(59, 130, 246, 0.1);\n  color: #3b82f6;\n}\n.gress-loading {\n  display: flex;\n  align-items: center;\n  justify-content: center;\n}\n.gress-loading__spinner {\n  width: 40px;\n  height: 40px;\n  border: 3px solid #f0f2f5;\n  border-top-color: #6366f1;\n  border-radius: 9999px;\n  animation: spin 0.8s linear infinite;\n}\n.gress-loading--sm .gress-loading__spinner {\n  width: 24px;\n  height: 24px;\n  border-width: 2px;\n}\n.gress-loading--lg .gress-loading__spinner {\n  width: 56px;\n  height: 56px;\n  border-width: 4px;\n}\n@keyframes spin {\n  to {\n    transform: rotate(360deg);\n  }\n}\n.gress-empty {\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  flex-direction: column;\n  gap: 16px;\n  padding: 48px 24px;\n  text-align: center;\n}\n.gress-empty__icon {\n  font-size: 64px;\n  color: #c9cdd4;\n}\n.gress-empty__title {\n  font-size: 16px;\n  font-weight: 600;\n  color: #4e5969;\n}\n.gress-empty__description {\n  font-size: 13px;\n  color: #86909c;\n  max-width: 400px;\n}\n.gress-divider {\n  height: 1px;\n  background: #f0f2f5;\n  margin: 24px 0;\n}\n.gress-divider--vertical {\n  width: 1px;\n  height: auto;\n  margin: 0 16px;\n}\n.gress-divider--dashed {\n  border-top: 1px dashed #e5e6eb;\n  background: none;\n}\n* {\n  box-sizing: border-box;\n  margin: 0;\n  padding: 0;\n}\nhtml {\n  font-size: 16px;\n  -webkit-font-smoothing: antialiased;\n  -moz-osx-font-smoothing: grayscale;\n  text-rendering: optimizeLegibility;\n}\nbody {\n  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, "Noto Sans", sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji";\n  font-size: 13px;\n  line-height: 1.5;\n  color: #1f2329;\n  background: #f7f8fa;\n  min-height: 100vh;\n}\n::-webkit-scrollbar {\n  width: 8px;\n  height: 8px;\n}\n::-webkit-scrollbar-track {\n  background: transparent;\n}\n::-webkit-scrollbar-thumb {\n  background: #d4d4d4;\n  border-radius: 9999px;\n}\n::-webkit-scrollbar-thumb:hover {\n  background: #a3a3a3;\n}\n::selection {\n  background: rgba(99, 102, 241, 0.2);\n  color: #1f2329;\n}\na {\n  color: #6366f1;\n  text-decoration: none;\n  transition: color 0.2s ease;\n}\na:hover {\n  color: #4f46e5;\n}\ncode {\n  font-family: "JetBrains Mono", ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;\n  font-size: 0.9em;\n  padding: 2px 6px;\n  background: rgba(99, 102, 241, 0.08);\n  border-radius: 6px;\n  color: #6366f1;\n}\npre {\n  font-family: "JetBrains Mono", ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;\n  padding: 16px;\n  background: #f2f3f5;\n  border-radius: 12px;\n  overflow-x: auto;\n}\npre code {\n  padding: 0;\n  background: none;\n}\n.workflow-editor {\n  width: 100%;\n  height: 100%;\n  position: relative;\n  background: #f7f8fa;\n  overflow: hidden;\n}\n.workflow-canvas {\n  width: 100%;\n  height: 100%;\n  position: relative;\n  background-color: #f7f8fa;\n  background-image: linear-gradient(rgba(99, 102, 241, 0.03) 1px, transparent 1px), linear-gradient(90deg, rgba(99, 102, 241, 0.03) 1px, transparent 1px);\n  background-size: 24px 24px;\n}\n.workflow-node {\n  position: absolute;\n  min-width: 260px;\n  background: #ffffff;\n  border-radius: 16px;\n  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);\n  cursor: move;\n  transition: all 0.2s ease;\n  border: 2px solid transparent;\n}\n.workflow-node:hover {\n  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);\n  transform: translateY(-2px);\n}\n.workflow-node.selected {\n  border-color: #6366f1;\n  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);\n}\n.workflow-node.error {\n  border-color: #ef4444;\n}\n.workflow-node.success {\n  border-color: #10b981;\n}\n.workflow-connection {\n  position: absolute;\n  pointer-events: none;\n  z-index: 1;\n}\n.workflow-connection.selected {\n  stroke: #6366f1 !important;\n  stroke-width: 3px !important;\n}\n.chat-container {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #ffffff;\n}\n.chat-messages {\n  flex: 1;\n  overflow-y: auto;\n  padding: 24px;\n}\n.chat-messages::-webkit-scrollbar {\n  width: 6px;\n  height: 6px;\n}\n.chat-messages::-webkit-scrollbar-track {\n  background: transparent;\n  border-radius: 9999px;\n}\n.chat-messages::-webkit-scrollbar-thumb {\n  background: #d4d4d4;\n  border-radius: 9999px;\n}\n.chat-messages::-webkit-scrollbar-thumb:hover {\n  background: rgb(186.5, 186.5, 186.5);\n}\n.chat-messages .chat-message {\n  background: #ffffff;\n  border-radius: 16px;\n  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.06);\n  padding: 16px;\n  border: 1px solid #f0f2f5;\n  margin-bottom: 16px;\n  max-width: 80%;\n}\n.chat-messages .chat-message--user {\n  margin-left: auto;\n  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);\n  color: #ffffff;\n}\n.chat-messages .chat-message--assistant {\n  margin-right: auto;\n  background: #ffffff;\n}\n.chat-messages .chat-message--system {\n  margin: 0 auto;\n  background: #f2f3f5;\n  color: #4e5969;\n  text-align: center;\n  max-width: 60%;\n}\n.chat-input {\n  border-top: 1px solid #f0f2f5;\n  padding: 24px;\n  background: #ffffff;\n}\n.chat-input__wrapper {\n  display: flex;\n  align-items: center;\n  gap: 12px;\n}\n.chat-input__field {\n  width: 100%;\n  border: 1px solid #e5e6eb;\n  border-radius: 8px;\n  background: #ffffff;\n  color: #1f2329;\n  font-size: 13px;\n  transition: all 0.2s ease;\n}\n.chat-input__field:hover {\n  border-color: #dcdfe6;\n}\n.chat-input__field:focus {\n  outline: none;\n  border-color: #6366f1;\n  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);\n}\n.chat-input__field:disabled {\n  background: #f2f3f5;\n  color: #e5e6eb;\n  cursor: not-allowed;\n}\n.chat-input__field::placeholder {\n  color: #86909c;\n}\n.chat-input__field {\n  flex: 1;\n  height: 40px;\n  padding: 0 16px;\n}\n.chat-input__button {\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  gap: 8px;\n  border: none;\n  border-radius: 8px;\n  font-weight: 500;\n  cursor: pointer;\n  transition: all 0.2s ease;\n  white-space: nowrap;\n  user-select: none;\n  -webkit-user-select: none;\n  -moz-user-select: none;\n  -ms-user-select: none;\n}\n.chat-input__button:disabled {\n  opacity: 0.5;\n  cursor: not-allowed;\n}\n.chat-input__button {\n  background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);\n  color: #ffffff;\n  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.25);\n}\n.chat-input__button:hover:not(:disabled) {\n  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);\n  transform: translateY(-1px);\n}\n.chat-input__button:active:not(:disabled) {\n  transform: translateY(0);\n}\n.chat-input__button {\n  height: 40px;\n  padding: 0 20px;\n}\n.workflow-list {\n  padding: 32px;\n}\n.workflow-list__header {\n  display: flex;\n  align-items: center;\n  justify-content: space-between;\n  margin-bottom: 32px;\n}\n.workflow-list__title {\n  font-size: 24px;\n  font-weight: 700;\n  color: #1f2329;\n}\n.workflow-list__grid {\n  display: grid;\n  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));\n  gap: 24px;\n}\n.workflow-list__item {\n  background: #ffffff;\n  border-radius: 16px;\n  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.08);\n  padding: 24px;\n  border: 1px solid #f0f2f5;\n  transition: all 0.2s ease;\n}\n.workflow-list__item:hover {\n  transform: translateY(-2px);\n  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);\n}\n.workflow-list__item {\n  cursor: pointer;\n}\n.workflow-list__item:hover {\n  border-color: #6366f1;\n}\n.workflow-list__item-header {\n  display: flex;\n  align-items: center;\n  justify-content: space-between;\n  margin-bottom: 12px;\n}\n.workflow-list__item-title {\n  font-size: 16px;\n  font-weight: 600;\n  color: #1f2329;\n  overflow: hidden;\n  text-overflow: ellipsis;\n  white-space: nowrap;\n}\n.workflow-list__item-desc {\n  font-size: 12px;\n  color: #86909c;\n  line-height: 1.6;\n  margin-bottom: 16px;\n  display: -webkit-box;\n  -webkit-line-clamp: 2;\n  -webkit-box-orient: vertical;\n  overflow: hidden;\n  text-overflow: ellipsis;\n}\n.workflow-list__item-footer {\n  display: flex;\n  align-items: center;\n  justify-content: space-between;\n  padding-top: 12px;\n  border-top: 1px solid #f0f2f5;\n}\n.workflow-list__item-meta {\n  font-size: 11px;\n  color: #86909c;\n}\n@media (max-width: 767px) {\n  .workflow-list {\n    padding: 24px;\n  }\n  .workflow-list__grid {\n    grid-template-columns: 1fr;\n  }\n  .chat-messages .chat-message {\n    max-width: 90%;\n  }\n}\n@media (max-width: 639px) {\n  .workflow-list {\n    padding: 16px;\n  }\n  .chat-input {\n    padding: 16px;\n  }\n}\n@keyframes fadeIn {\n  from {\n    opacity: 0;\n  }\n  to {\n    opacity: 1;\n  }\n}\n@keyframes slideInUp {\n  from {\n    opacity: 0;\n    transform: translateY(20px);\n  }\n  to {\n    opacity: 1;\n    transform: translateY(0);\n  }\n}\n@keyframes scaleIn {\n  from {\n    opacity: 0;\n    transform: scale(0.95);\n  }\n  to {\n    opacity: 1;\n    transform: scale(1);\n  }\n}\n@keyframes pulse {\n  0%, 100% {\n    opacity: 1;\n  }\n  50% {\n    opacity: 0.5;\n  }\n}\n@keyframes spin {\n  to {\n    transform: rotate(360deg);\n  }\n}\n@media print {\n  body {\n    background: white;\n  }\n  .workflow-canvas {\n    background: white;\n  }\n  .workflow-node {\n    box-shadow: none;\n    border: 1px solid #e5e6eb;\n  }\n}@keyframes fadeIn-65bd80d8 {\nfrom {\n    opacity: 0;\n}\nto {\n    opacity: 1;\n}\n}\n@keyframes slideInUp-65bd80d8 {\nfrom {\n    opacity: 0;\n    transform: translateY(20px);\n}\nto {\n    opacity: 1;\n    transform: translateY(0);\n}\n}\n@keyframes scaleIn-65bd80d8 {\nfrom {\n    opacity: 0;\n    transform: scale(0.9);\n}\nto {\n    opacity: 1;\n    transform: scale(1);\n}\n}\n@keyframes pulse-65bd80d8 {\n0%, 100% {\n    opacity: 1;\n}\n50% {\n    opacity: 0.5;\n}\n}\n.page-container[data-v-65bd80d8] {\n  height: 100%;\n  display: flex;\n  flex-direction: column;\n  overflow: hidden;\n  background: var(--bg-secondary, #f7f8fa);\n}@keyframes fadeIn-7bbf9d25 {\nfrom {\n    opacity: 0;\n}\nto {\n    opacity: 1;\n}\n}\n@keyframes slideInUp-7bbf9d25 {\nfrom {\n    opacity: 0;\n    transform: translateY(20px);\n}\nto {\n    opacity: 1;\n    transform: translateY(0);\n}\n}\n@keyframes scaleIn-7bbf9d25 {\nfrom {\n    opacity: 0;\n    transform: scale(0.9);\n}\nto {\n    opacity: 1;\n    transform: scale(1);\n}\n}\n@keyframes pulse-7bbf9d25 {\n0%, 100% {\n    opacity: 1;\n}\n50% {\n    opacity: 0.5;\n}\n}\n.page-content[data-v-7bbf9d25] {\n  flex: 1;\n  overflow-y: auto;\n  padding: var(--spacing-2xl, 24px);\n  display: flex;\n  flex-direction: column;\n  gap: var(--spacing-lg, 16px);\n}\n.page-content[data-v-7bbf9d25]::-webkit-scrollbar {\n  width: 6px;\n  height: 6px;\n}\n.page-content[data-v-7bbf9d25]::-webkit-scrollbar-track {\n  background: transparent;\n  border-radius: var(--radius-full, 9999px);\n}\n.page-content[data-v-7bbf9d25]::-webkit-scrollbar-thumb {\n  background: var(--gray-300, #d4d4d4);\n  border-radius: var(--radius-full, 9999px);\n}\n.page-content[data-v-7bbf9d25]::-webkit-scrollbar-thumb:hover {\n  background: var(--gray-400, #a3a3a3);\n}\n@media (max-width: 768px) {\n.page-content[data-v-7bbf9d25] {\n    padding: var(--spacing-lg, 16px);\n}\n}@keyframes fadeIn-4d82a654 {\nfrom {\n    opacity: 0;\n}\nto {\n    opacity: 1;\n}\n}\n@keyframes slideInUp-4d82a654 {\nfrom {\n    opacity: 0;\n    transform: translateY(20px);\n}\nto {\n    opacity: 1;\n    transform: translateY(0);\n}\n}\n@keyframes scaleIn-4d82a654 {\nfrom {\n    opacity: 0;\n    transform: scale(0.9);\n}\nto {\n    opacity: 1;\n    transform: scale(1);\n}\n}\n@keyframes pulse-4d82a654 {\n0%, 100% {\n    opacity: 1;\n}\n50% {\n    opacity: 0.5;\n}\n}\n.page-header[data-v-4d82a654] {\n  background: var(--bg-primary, #ffffff);\n  border-bottom: 1px solid var(--border-secondary, #f0f2f5);\n  padding: 0 var(--spacing-3xl, 32px);\n  height: 64px;\n  flex-shrink: 0;\n  display: flex;\n  align-items: center;\n}\n.page-header__content[data-v-4d82a654] {\n  display: flex;\n  justify-content: space-between;\n  align-items: center;\n  width: 100%;\n}\n.page-header__prefix[data-v-4d82a654] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-md, 12px);\n  flex: 1;\n}\n.page-header__left[data-v-4d82a654] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-md, 12px);\n}\n.page-header__breadcrumb[data-v-4d82a654] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-md, 12px);\n  flex-wrap: wrap;\n}\n.page-header .breadcrumb-item[data-v-4d82a654] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-xs, 4px);\n  color: var(--text-tertiary, #86909c);\n  font-size: var(--font-size-base, 13px);\n  font-weight: var(--font-weight-medium, 500);\n  transition: color 0.2s ease;\n}\n.page-header .breadcrumb-item[data-v-4d82a654]:hover {\n  color: var(--text-secondary, #4e5969);\n}\n.page-header .breadcrumb-item--current[data-v-4d82a654] {\n  color: var(--text-primary, #1f2329);\n  font-size: var(--font-size-lg, 16px);\n  font-weight: var(--font-weight-semibold, 600);\n}\n.page-header .breadcrumb-separator[data-v-4d82a654] {\n  color: var(--text-quaternary, #c9cdd4);\n  font-size: var(--font-size-base, 13px);\n  user-select: none;\n}\n.page-header__actions[data-v-4d82a654] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-md, 12px);\n}\n@media (max-width: 768px) {\n.page-header[data-v-4d82a654] {\n    padding: var(--spacing-md, 12px);\n}\n.page-header__content[data-v-4d82a654] {\n    flex-direction: column;\n    align-items: flex-start;\n    gap: var(--spacing-md, 12px);\n}\n.page-header__prefix[data-v-4d82a654] {\n    width: 100%;\n}\n.page-header__actions[data-v-4d82a654] {\n    width: 100%;\n    justify-content: flex-end;\n}\n}\n@media (max-width: 640px) {\n.page-header[data-v-4d82a654] {\n    padding: var(--spacing-sm, 8px) var(--spacing-md, 12px);\n}\n.page-header .breadcrumb-item[data-v-4d82a654] {\n    font-size: var(--font-size-sm, 12px);\n}\n.page-header .breadcrumb-item--current[data-v-4d82a654] {\n    font-size: var(--font-size-base, 13px);\n}\n.page-header .breadcrumb-separator[data-v-4d82a654] {\n    font-size: var(--font-size-sm, 12px);\n}\n}@keyframes fadeIn-f199a048 {\nfrom {\n    opacity: 0;\n}\nto {\n    opacity: 1;\n}\n}\n@keyframes slideInUp-f199a048 {\nfrom {\n    opacity: 0;\n    transform: translateY(20px);\n}\nto {\n    opacity: 1;\n    transform: translateY(0);\n}\n}\n@keyframes scaleIn-f199a048 {\nfrom {\n    opacity: 0;\n    transform: scale(0.9);\n}\nto {\n    opacity: 1;\n    transform: scale(1);\n}\n}\n@keyframes pulse-f199a048 {\n0%, 100% {\n    opacity: 1;\n}\n50% {\n    opacity: 0.5;\n}\n}\n.card-list[data-v-f199a048] {\n  display: grid;\n  gap: var(--spacing-lg, 16px);\n}\n.card-list--medium[data-v-f199a048] {\n  grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));\n}\n.card-list--small[data-v-f199a048] {\n  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));\n}\n.card-list--large[data-v-f199a048] {\n  grid-template-columns: repeat(auto-fill, minmax(480px, 1fr));\n}\n@media (max-width: 768px) {\n.card-list[data-v-f199a048] {\n    grid-template-columns: 1fr;\n}\n}@keyframes fadeIn-bf918fa3 {\nfrom {\n    opacity: 0;\n}\nto {\n    opacity: 1;\n}\n}\n@keyframes slideInUp-bf918fa3 {\nfrom {\n    opacity: 0;\n    transform: translateY(20px);\n}\nto {\n    opacity: 1;\n    transform: translateY(0);\n}\n}\n@keyframes scaleIn-bf918fa3 {\nfrom {\n    opacity: 0;\n    transform: scale(0.9);\n}\nto {\n    opacity: 1;\n    transform: scale(1);\n}\n}\n@keyframes pulse-bf918fa3 {\n0%, 100% {\n    opacity: 1;\n}\n50% {\n    opacity: 0.5;\n}\n}\n.page-pagination[data-v-bf918fa3] {\n  display: flex;\n  justify-content: flex-end;\n  padding: var(--spacing-lg, 16px) 0;\n  margin-top: var(--spacing-lg, 16px);\n}\n.pagination-controls[data-v-bf918fa3] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-md, 12px);\n}\n.pagination-btn[data-v-bf918fa3] {\n  padding: var(--spacing-sm, 8px) var(--spacing-lg, 16px);\n  background: var(--bg-primary, #ffffff);\n  border: 1px solid var(--border-primary, #e5e6eb);\n  border-radius: var(--radius-md, 8px);\n  color: var(--text-primary, #1f2329);\n  font-size: var(--font-size-sm, 12px);\n  cursor: pointer;\n  transition: all 0.2s ease;\n}\n.pagination-btn[data-v-bf918fa3]:hover:not(:disabled) {\n  background: var(--bg-secondary, #f7f8fa);\n  border-color: var(--primary, #6366f1);\n  color: var(--primary, #6366f1);\n}\n.pagination-btn[data-v-bf918fa3]:disabled {\n  opacity: 0.5;\n  cursor: not-allowed;\n}\n.pagination-info[data-v-bf918fa3] {\n  color: var(--text-secondary, #4e5969);\n  font-size: var(--font-size-sm, 12px);\n  white-space: nowrap;\n}\n.pagination-select[data-v-bf918fa3] {\n  padding: var(--spacing-sm, 8px) var(--spacing-md, 12px);\n  background: var(--bg-primary, #ffffff);\n  border: 1px solid var(--border-primary, #e5e6eb);\n  border-radius: var(--radius-md, 8px);\n  color: var(--text-primary, #1f2329);\n  font-size: var(--font-size-sm, 12px);\n  cursor: pointer;\n  transition: all 0.2s ease;\n}\n.pagination-select[data-v-bf918fa3]:hover {\n  border-color: var(--primary, #6366f1);\n}\n.pagination-select[data-v-bf918fa3]:focus {\n  outline: none;\n  border-color: var(--primary, #6366f1);\n  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);\n}@keyframes fadeIn-c4c4657a {\nfrom {\n    opacity: 0;\n}\nto {\n    opacity: 1;\n}\n}\n@keyframes slideInUp-c4c4657a {\nfrom {\n    opacity: 0;\n    transform: translateY(20px);\n}\nto {\n    opacity: 1;\n    transform: translateY(0);\n}\n}\n@keyframes scaleIn-c4c4657a {\nfrom {\n    opacity: 0;\n    transform: scale(0.9);\n}\nto {\n    opacity: 1;\n    transform: scale(1);\n}\n}\n@keyframes pulse-c4c4657a {\n0%, 100% {\n    opacity: 1;\n}\n50% {\n    opacity: 0.5;\n}\n}\n.log-viewer[data-v-c4c4657a] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: var(--bg-secondary, #f8f9fa);\n  border-radius: var(--radius-md, 8px);\n  overflow: hidden;\n  border: 1px solid var(--border-secondary, #e8e8e8);\n}\n.log-viewer.log-viewer--dark[data-v-c4c4657a] {\n  background: #1e1e1e;\n  border-color: #3e3e3e;\n}\n.log-viewer.log-viewer--dark .log-toolbar[data-v-c4c4657a] {\n  background: #2d2d2d;\n  border-bottom-color: #3e3e3e;\n}\n.log-viewer.log-viewer--dark .log-content[data-v-c4c4657a] {\n  background: #1e1e1e;\n}\n.log-viewer.log-viewer--dark .log-line[data-v-c4c4657a]:hover {\n  background: rgba(255, 255, 255, 0.05);\n}\n.log-viewer.log-viewer--dark .log-timestamp[data-v-c4c4657a] {\n  color: #8c8c8c;\n}\n.log-viewer.log-viewer--dark .log-thread[data-v-c4c4657a] {\n  color: #69c0ff;\n}\n.log-viewer.log-viewer--dark .log-class[data-v-c4c4657a] {\n  color: #b37feb;\n}\n.log-viewer.log-viewer--dark .log-message[data-v-c4c4657a] {\n  color: #d4d4d4;\n}\n.log-viewer.log-viewer--dark .log-empty[data-v-c4c4657a] {\n  color: #8c8c8c;\n}\n.log-viewer.log-viewer--dark .toolbar-btn[data-v-c4c4657a] {\n  background: #3e3e3e;\n  color: #d4d4d4;\n  border-color: #4e4e4e;\n}\n.log-viewer.log-viewer--dark .toolbar-btn[data-v-c4c4657a]:hover {\n  background: #4e4e4e;\n}\n.log-viewer.log-viewer--dark .toolbar-select[data-v-c4c4657a] {\n  background: #3e3e3e;\n  color: #d4d4d4;\n  border-color: #4e4e4e;\n}\n.log-toolbar[data-v-c4c4657a] {\n  padding: 12px;\n  background: var(--bg-primary, #ffffff);\n  border-bottom: 1px solid var(--border-secondary, #e8e8e8);\n  flex-shrink: 0;\n}\n.toolbar-group[data-v-c4c4657a] {\n  display: flex;\n  gap: 8px;\n  align-items: center;\n  flex-wrap: wrap;\n}\n.toolbar-btn[data-v-c4c4657a] {\n  display: flex;\n  align-items: center;\n  gap: 4px;\n  padding: 6px 12px;\n  background: var(--bg-primary, #ffffff);\n  border: 1px solid var(--border-primary, #e5e6eb);\n  border-radius: var(--radius-md, 8px);\n  color: var(--text-primary, #1f2329);\n  font-size: var(--font-size-sm, 12px);\n  cursor: pointer;\n  transition: all 0.2s ease;\n}\n.toolbar-btn[data-v-c4c4657a]:hover {\n  background: var(--bg-secondary, #f7f8fa);\n  border-color: var(--primary, #6366f1);\n}\n.toolbar-btn .btn-icon[data-v-c4c4657a] {\n  font-size: 14px;\n}\n.toolbar-select[data-v-c4c4657a] {\n  padding: 6px 12px;\n  background: var(--bg-primary, #ffffff);\n  border: 1px solid var(--border-primary, #e5e6eb);\n  border-radius: var(--radius-md, 8px);\n  color: var(--text-primary, #1f2329);\n  font-size: var(--font-size-sm, 12px);\n  cursor: pointer;\n  transition: all 0.2s ease;\n}\n.toolbar-select[data-v-c4c4657a]:hover {\n  border-color: var(--primary, #6366f1);\n}\n.toolbar-select[data-v-c4c4657a]:focus {\n  outline: none;\n  border-color: var(--primary, #6366f1);\n  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);\n}\n.log-content[data-v-c4c4657a] {\n  flex: 1;\n  padding: 12px;\n  overflow-y: auto;\n  overflow-x: hidden;\n}\n.log-content[data-v-c4c4657a]::-webkit-scrollbar {\n  width: 6px;\n}\n.log-content[data-v-c4c4657a]::-webkit-scrollbar-thumb {\n  background: var(--gray-300, #d4d4d4);\n  border-radius: var(--radius-full, 9999px);\n}\n.log-content[data-v-c4c4657a]::-webkit-scrollbar-thumb:hover {\n  background: var(--gray-400, #a3a3a3);\n}\n.log-loading[data-v-c4c4657a] {\n  display: flex;\n  flex-direction: column;\n  align-items: center;\n  justify-content: center;\n  height: 200px;\n  gap: 12px;\n  color: var(--text-tertiary, #86909c);\n}\n.loading-spinner[data-v-c4c4657a] {\n  width: 32px;\n  height: 32px;\n  border: 3px solid var(--border-secondary, #f0f2f5);\n  border-top-color: var(--primary, #6366f1);\n  border-radius: var(--radius-full, 9999px);\n  animation: spin-c4c4657a 0.8s linear infinite;\n}\n@keyframes spin-c4c4657a {\nto {\n    transform: rotate(360deg);\n}\n}\n.log-list[data-v-c4c4657a] {\n  display: flex;\n  flex-direction: column;\n  gap: 0;\n}\n.log-line[data-v-c4c4657a] {\n  padding: 4px 8px;\n  font-family: "Consolas", "Monaco", "Courier New", monospace;\n  font-size: 12px;\n  line-height: 1.6;\n  white-space: pre-wrap;\n  word-break: break-word;\n  border-left: 2px solid transparent;\n  transition: all 0.15s ease;\n}\n.log-line[data-v-c4c4657a]:hover {\n  background: rgba(0, 0, 0, 0.03);\n  border-left-color: var(--primary, #6366f1);\n}\n.log-line.log-level-trace .log-level[data-v-c4c4657a] {\n  color: #8c8c8c;\n}\n.log-line.log-level-debug .log-level[data-v-c4c4657a] {\n  color: #1890ff;\n}\n.log-line.log-level-info .log-level[data-v-c4c4657a] {\n  color: #52c41a;\n}\n.log-line.log-level-warn[data-v-c4c4657a] {\n  background: rgba(250, 173, 20, 0.05);\n}\n.log-line.log-level-warn .log-level[data-v-c4c4657a] {\n  color: #faad14;\n}\n.log-line.log-level-error[data-v-c4c4657a], .log-line.log-level-fatal[data-v-c4c4657a] {\n  background: rgba(245, 34, 45, 0.05);\n}\n.log-line.log-level-error .log-level[data-v-c4c4657a], .log-line.log-level-fatal .log-level[data-v-c4c4657a] {\n  color: #f5222d;\n  font-weight: 600;\n}\n.log-timestamp[data-v-c4c4657a] {\n  color: #8c8c8c;\n  margin-right: 8px;\n  font-weight: 500;\n}\n.log-thread[data-v-c4c4657a] {\n  color: #1890ff;\n  margin-right: 8px;\n  font-style: italic;\n}\n.log-level[data-v-c4c4657a] {\n  display: inline-block;\n  min-width: 50px;\n  margin-right: 8px;\n  font-weight: 600;\n  text-align: left;\n}\n.log-class[data-v-c4c4657a] {\n  color: #722ed1;\n  margin-right: 8px;\n}\n.log-separator[data-v-c4c4657a] {\n  color: #8c8c8c;\n  margin-right: 8px;\n}\n.log-message[data-v-c4c4657a] {\n  color: var(--text-primary, #262626);\n  flex: 1;\n}\n.log-error-detail[data-v-c4c4657a] {\n  display: block;\n  margin-top: 4px;\n  padding-left: 24px;\n  color: #f5222d;\n}\n.log-error-label[data-v-c4c4657a] {\n  font-weight: 600;\n  margin-right: 8px;\n}\n.log-error-message[data-v-c4c4657a] {\n  font-style: italic;\n}\n.log-empty[data-v-c4c4657a] {\n  display: flex;\n  flex-direction: column;\n  align-items: center;\n  justify-content: center;\n  height: 200px;\n  gap: 12px;\n  color: var(--text-tertiary, #86909c);\n}\n.empty-icon[data-v-c4c4657a] {\n  font-size: 48px;\n  opacity: 0.5;\n}\n.empty-text[data-v-c4c4657a] {\n  font-size: var(--font-size-base, 13px);\n}@keyframes fadeIn-8db9aa68 {\nfrom {\n    opacity: 0;\n}\nto {\n    opacity: 1;\n}\n}\n@keyframes slideInUp-8db9aa68 {\nfrom {\n    opacity: 0;\n    transform: translateY(20px);\n}\nto {\n    opacity: 1;\n    transform: translateY(0);\n}\n}\n@keyframes scaleIn-8db9aa68 {\nfrom {\n    opacity: 0;\n    transform: scale(0.9);\n}\nto {\n    opacity: 1;\n    transform: scale(1);\n}\n}\n@keyframes pulse-8db9aa68 {\n0%, 100% {\n    opacity: 1;\n}\n50% {\n    opacity: 0.5;\n}\n}\n.filter-card[data-v-8db9aa68] {\n  background: var(--bg-primary, #ffffff);\n  border-radius: var(--radius-lg, 12px);\n  padding: var(--spacing-lg, 16px);\n  display: flex;\n  flex-direction: column;\n  gap: var(--spacing-md, 12px);\n}\n.filter-inline[data-v-8db9aa68] {\n  display: flex;\n  align-items: center;\n  justify-content: space-between;\n  gap: var(--spacing-lg, 16px);\n  flex-wrap: wrap;\n}\n.filter-inline__fields[data-v-8db9aa68] {\n  display: flex;\n  gap: var(--spacing-lg, 16px);\n  flex: 1;\n  min-width: 240px;\n  flex-wrap: wrap;\n}\n.filter-inline__field[data-v-8db9aa68] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-sm, 8px);\n  min-width: 220px;\n}\n.field-label[data-v-8db9aa68] {\n  color: var(--text-secondary, #4e5969);\n  font-size: var(--font-size-sm, 12px);\n  white-space: nowrap;\n}\n.filter-input[data-v-8db9aa68] {\n  flex: 1;\n  padding: var(--spacing-sm, 8px) var(--spacing-md, 12px);\n  border: 1px solid var(--border-primary, #e5e6eb);\n  border-radius: var(--radius-md, 8px);\n  background: var(--bg-primary, #ffffff);\n  color: var(--text-primary, #1f2329);\n  font-size: var(--font-size-base, 13px);\n  transition: all 0.2s ease;\n}\n.filter-input[data-v-8db9aa68]:hover {\n  border-color: var(--border-tertiary, #dcdfe6);\n}\n.filter-input[data-v-8db9aa68]:focus {\n  outline: none;\n  border-color: var(--primary, #6366f1);\n  box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.1);\n}\n.filter-input[data-v-8db9aa68]::placeholder {\n  color: var(--text-tertiary, #86909c);\n}\n.filter-inline__actions[data-v-8db9aa68] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-sm, 8px);\n}\n.filter-inline__actions.align-left[data-v-8db9aa68] {\n  justify-content: flex-start;\n}\n.filter-inline__actions.align-right[data-v-8db9aa68] {\n  justify-content: flex-end;\n}\n.filter-btn[data-v-8db9aa68] {\n  padding: var(--spacing-sm, 8px) var(--spacing-lg, 16px);\n  border: none;\n  border-radius: var(--radius-md, 8px);\n  font-size: var(--font-size-sm, 12px);\n  font-weight: var(--font-weight-medium, 500);\n  cursor: pointer;\n  transition: all 0.2s ease;\n  white-space: nowrap;\n}\n.filter-btn--primary[data-v-8db9aa68] {\n  background: linear-gradient(135deg, var(--primary, #6366f1) 0%, var(--secondary, #8b5cf6) 100%);\n  color: var(--text-white, #ffffff);\n  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.25);\n}\n.filter-btn--primary[data-v-8db9aa68]:hover {\n  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);\n  transform: translateY(-1px);\n}\n.filter-btn--secondary[data-v-8db9aa68] {\n  background: var(--bg-secondary, #f7f8fa);\n  color: var(--text-primary, #1f2329);\n  border: 1px solid var(--border-primary, #e5e6eb);\n}\n.filter-btn--secondary[data-v-8db9aa68]:hover {\n  background: var(--bg-tertiary, #f2f3f5);\n  border-color: var(--border-tertiary, #dcdfe6);\n}\n.inline-toggle[data-v-8db9aa68] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-xs, 4px);\n  padding: var(--spacing-xs, 4px) var(--spacing-sm, 8px);\n  background: none;\n  border: 1px solid rgba(99, 102, 241, 0.3);\n  border-radius: var(--radius-full, 9999px);\n  color: var(--primary, #6366f1);\n  font-size: var(--font-size-sm, 12px);\n  cursor: pointer;\n  transition: all 0.2s ease;\n}\n.inline-toggle[data-v-8db9aa68]:hover {\n  background: rgba(99, 102, 241, 0.08);\n}\n.inline-toggle .toggle-icon[data-v-8db9aa68] {\n  font-size: 10px;\n}\n.filter-grid[data-v-8db9aa68] {\n  display: grid;\n  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));\n  gap: var(--spacing-md, 12px);\n  padding-top: var(--spacing-sm, 8px);\n  border-top: 1px dashed var(--border-secondary, #f0f2f5);\n  overflow: hidden;\n}\n.filter-grid__field[data-v-8db9aa68] {\n  display: flex;\n  align-items: center;\n  gap: var(--spacing-sm, 8px);\n}\n.filter-grid__hint[data-v-8db9aa68] {\n  grid-column: 1/-1;\n  font-size: var(--font-size-xs, 11px);\n  color: var(--text-tertiary, #86909c);\n}@keyframes fadeIn-c1a551dc {\nfrom {\n    opacity: 0;\n}\nto {\n    opacity: 1;\n}\n}\n@keyframes slideInUp-c1a551dc {\nfrom {\n    opacity: 0;\n    transform: translateY(20px);\n}\nto {\n    opacity: 1;\n    transform: translateY(0);\n}\n}\n@keyframes scaleIn-c1a551dc {\nfrom {\n    opacity: 0;\n    transform: scale(0.9);\n}\nto {\n    opacity: 1;\n    transform: scale(1);\n}\n}\n@keyframes pulse-c1a551dc {\n0%, 100% {\n    opacity: 1;\n}\n50% {\n    opacity: 0.5;\n}\n}\n.fade-scale-enter-active[data-v-c1a551dc],\n.fade-scale-leave-active[data-v-c1a551dc] {\n  transition: opacity 0.25s ease, transform 0.25s ease;\n}\n.fade-scale-enter-from[data-v-c1a551dc],\n.fade-scale-leave-to[data-v-c1a551dc] {\n  opacity: 0;\n  transform: scale(0.95);\n}\n.slide-left-enter-active[data-v-c1a551dc],\n.slide-left-leave-active[data-v-c1a551dc],\n.slide-right-enter-active[data-v-c1a551dc],\n.slide-right-leave-active[data-v-c1a551dc],\n.slide-top-enter-active[data-v-c1a551dc],\n.slide-top-leave-active[data-v-c1a551dc],\n.slide-bottom-enter-active[data-v-c1a551dc],\n.slide-bottom-leave-active[data-v-c1a551dc] {\n  transition: opacity 0.25s ease, transform 0.25s ease;\n}\n.slide-left-enter-from[data-v-c1a551dc],\n.slide-left-leave-to[data-v-c1a551dc] {\n  opacity: 0;\n  transform: translateX(-20px);\n}\n.slide-right-enter-from[data-v-c1a551dc],\n.slide-right-leave-to[data-v-c1a551dc] {\n  opacity: 0;\n  transform: translateX(20px);\n}\n.slide-top-enter-from[data-v-c1a551dc],\n.slide-top-leave-to[data-v-c1a551dc] {\n  opacity: 0;\n  transform: translateY(-20px);\n}\n.slide-bottom-enter-from[data-v-c1a551dc],\n.slide-bottom-leave-to[data-v-c1a551dc] {\n  opacity: 0;\n  transform: translateY(20px);\n}\n.universal-modal-wrapper[data-v-c1a551dc] {\n  position: fixed;\n  inset: 0;\n  display: flex;\n  z-index: 1000;\n}\n.universal-modal-wrapper.with-mask[data-v-c1a551dc] {\n  background: rgba(0, 0, 0, 0.45);\n}\n.universal-modal-wrapper.no-mask[data-v-c1a551dc] {\n  background: transparent;\n  pointer-events: none;\n}\n.universal-modal-wrapper.position-center[data-v-c1a551dc] {\n  align-items: center;\n  justify-content: center;\n  padding: 24px;\n}\n.universal-modal-wrapper.position-left[data-v-c1a551dc] {\n  justify-content: flex-start;\n}\n.universal-modal-wrapper.position-right[data-v-c1a551dc] {\n  justify-content: flex-end;\n}\n.universal-modal-wrapper.position-top[data-v-c1a551dc] {\n  align-items: flex-start;\n}\n.universal-modal-wrapper.position-bottom[data-v-c1a551dc] {\n  align-items: flex-end;\n}\n.universal-modal-wrapper.fullscreen[data-v-c1a551dc] {\n  padding: 0;\n}\n.universal-modal[data-v-c1a551dc] {\n  position: relative;\n  background: var(--bg-primary, #ffffff);\n  border-radius: var(--radius-xl, 16px);\n  box-shadow: 0 20px 50px rgba(15, 23, 42, 0.14);\n  display: flex;\n  flex-direction: column;\n  overflow: hidden;\n  pointer-events: auto;\n  height: 100%;\n}\n.universal-modal.modal-center[data-v-c1a551dc] {\n  margin: auto;\n  height: auto;\n}\n.universal-modal.modal-left[data-v-c1a551dc], .universal-modal.modal-right[data-v-c1a551dc] {\n  position: absolute;\n}\n.universal-modal.modal-top[data-v-c1a551dc], .universal-modal.modal-bottom[data-v-c1a551dc] {\n  position: absolute;\n  max-width: calc(100vw - 48px);\n  height: auto;\n}\n.universal-modal.modal-fullscreen[data-v-c1a551dc] {\n  width: 100vw !important;\n  height: 100vh !important;\n  max-width: 100vw !important;\n  max-height: 100vh !important;\n  border-radius: 0;\n  top: 0 !important;\n  left: 0 !important;\n  right: 0 !important;\n  bottom: 0 !important;\n}\n.resize-handle[data-v-c1a551dc] {\n  position: absolute;\n  z-index: 10;\n}\n.resize-handle-top[data-v-c1a551dc], .resize-handle-bottom[data-v-c1a551dc] {\n  left: 0;\n  right: 0;\n  height: 4px;\n  cursor: ns-resize;\n}\n.resize-handle-top[data-v-c1a551dc]:hover, .resize-handle-bottom[data-v-c1a551dc]:hover {\n  background: rgba(99, 102, 241, 0.3);\n}\n.resize-handle-top[data-v-c1a551dc] {\n  top: 0;\n}\n.resize-handle-bottom[data-v-c1a551dc] {\n  bottom: 0;\n}\n.resize-handle-left[data-v-c1a551dc], .resize-handle-right[data-v-c1a551dc] {\n  top: 0;\n  bottom: 0;\n  width: 4px;\n  cursor: ew-resize;\n}\n.resize-handle-left[data-v-c1a551dc]:hover, .resize-handle-right[data-v-c1a551dc]:hover {\n  background: rgba(99, 102, 241, 0.3);\n}\n.resize-handle-left[data-v-c1a551dc] {\n  left: 0;\n}\n.resize-handle-right[data-v-c1a551dc] {\n  right: 0;\n}\n.modal-header[data-v-c1a551dc] {\n  padding: 16px 20px 12px;\n  display: flex;\n  justify-content: space-between;\n  align-items: flex-start;\n  gap: 12px;\n  border-bottom: 1px solid var(--border-secondary, #f2f3f5);\n  background: linear-gradient(180deg, rgba(248, 249, 255, 0.8), #fff);\n  flex-shrink: 0;\n}\n.header-content[data-v-c1a551dc] {\n  display: flex;\n  gap: 12px;\n  align-items: flex-start;\n  flex: 1;\n  min-width: 0;\n}\n.header-icon[data-v-c1a551dc] {\n  width: 32px;\n  height: 32px;\n  border-radius: 10px;\n  background: radial-gradient(circle at 30% 20%, #e6e8ff, #cdcfff);\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  flex-shrink: 0;\n}\n.header-icon .header-icon-img[data-v-c1a551dc] {\n  width: 24px;\n  height: 24px;\n  object-fit: contain;\n}\n.header-info[data-v-c1a551dc] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 4px;\n  min-width: 0;\n}\n.header-subtitle[data-v-c1a551dc] {\n  font-size: var(--font-size-sm, 12px);\n  color: var(--text-tertiary, #86909c);\n  line-height: 1.3;\n}\n.header-title[data-v-c1a551dc] {\n  font-size: var(--font-size-lg, 16px);\n  font-weight: var(--font-weight-semibold, 600);\n  color: var(--text-primary, #1f2329);\n  line-height: 1.3;\n  word-break: break-word;\n}\n.header-actions[data-v-c1a551dc] {\n  display: flex;\n  gap: 8px;\n  align-items: center;\n  flex-shrink: 0;\n}\n.header-btn[data-v-c1a551dc] {\n  width: 28px;\n  height: 28px;\n  border: none;\n  background: transparent;\n  border-radius: var(--radius-full, 9999px);\n  color: var(--text-tertiary, #86909c);\n  cursor: pointer;\n  transition: all 0.2s ease;\n  display: flex;\n  align-items: center;\n  justify-content: center;\n  font-size: 16px;\n}\n.header-btn[data-v-c1a551dc]:hover {\n  background: var(--bg-secondary, #f7f8fa);\n  color: var(--text-primary, #1f2329);\n}\n.modal-body[data-v-c1a551dc] {\n  flex: 1;\n  padding: 20px 24px;\n  overflow-y: auto;\n  overflow-x: hidden;\n  min-height: 0;\n  max-height: 100%;\n}\n.modal-body[data-v-c1a551dc]::-webkit-scrollbar {\n  width: 6px;\n}\n.modal-body[data-v-c1a551dc]::-webkit-scrollbar-thumb {\n  background: var(--gray-300, #d4d4d4);\n  border-radius: var(--radius-full, 9999px);\n}\n.modal-body[data-v-c1a551dc]::-webkit-scrollbar-thumb:hover {\n  background: var(--gray-400, #a3a3a3);\n}\n.modal-footer[data-v-c1a551dc] {\n  padding: 16px 24px;\n  border-top: 1px solid var(--border-secondary, #f2f3f5);\n  background: var(--bg-secondary, #f7f8fa);\n  flex-shrink: 0;\n}\n.footer-actions[data-v-c1a551dc] {\n  display: flex;\n  justify-content: flex-end;\n  gap: 12px;\n}\n.footer-btn[data-v-c1a551dc] {\n  padding: var(--spacing-sm, 8px) var(--spacing-lg, 16px);\n  border: none;\n  border-radius: var(--radius-md, 8px);\n  font-size: var(--font-size-sm, 12px);\n  font-weight: var(--font-weight-medium, 500);\n  cursor: pointer;\n  transition: all 0.2s ease;\n}\n.footer-btn--cancel[data-v-c1a551dc] {\n  background: var(--bg-secondary, #f7f8fa);\n  color: var(--text-primary, #1f2329);\n  border: 1px solid var(--border-primary, #e5e6eb);\n}\n.footer-btn--cancel[data-v-c1a551dc]:hover {\n  background: var(--bg-tertiary, #f2f3f5);\n}\n.footer-btn--confirm[data-v-c1a551dc] {\n  background: linear-gradient(135deg, var(--primary, #6366f1) 0%, var(--secondary, #8b5cf6) 100%);\n  color: var(--text-white, #ffffff);\n  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.25);\n}\n.footer-btn--confirm[data-v-c1a551dc]:hover {\n  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);\n  transform: translateY(-1px);\n}\n@media (max-width: 768px) {\n.universal-modal-wrapper.position-center[data-v-c1a551dc] {\n    padding: 16px;\n}\n.universal-modal.modal-left[data-v-c1a551dc], .universal-modal.modal-right[data-v-c1a551dc] {\n    max-height: calc(100vh - 32px);\n}\n.universal-modal.modal-top[data-v-c1a551dc], .universal-modal.modal-bottom[data-v-c1a551dc] {\n    max-width: calc(100vw - 32px);\n}\n.modal-header[data-v-c1a551dc] {\n    padding: 14px 16px 10px;\n}\n.modal-body[data-v-c1a551dc] {\n    padding: 16px 20px;\n}\n.modal-footer[data-v-c1a551dc] {\n    padding: 12px 20px;\n}\n}\n.loading-container[data-v-d3a7a28f] {\n  display: flex;\n  flex-direction: column;\n  align-items: center;\n  justify-content: center;\n  min-height: 200px;\n  padding: var(--spacing-4xl, 40px);\n}\n.loading-spinner[data-v-d3a7a28f] {\n  width: 40px;\n  height: 40px;\n  border: 3px solid var(--border-secondary, #f0f2f5);\n  border-top-color: var(--primary, #6366f1);\n  border-radius: var(--radius-full, 9999px);\n  animation: spin-d3a7a28f 0.8s linear infinite;\n}\n@keyframes spin-d3a7a28f {\nto {\n    transform: rotate(360deg);\n}\n}\n.loading-message[data-v-d3a7a28f] {\n  margin-top: var(--spacing-lg, 16px);\n  font-size: var(--font-size-sm, 12px);\n  color: var(--text-secondary, #4e5969);\n}\n\n.empty-state[data-v-a29f92b8] {\n  display: flex;\n  flex-direction: column;\n  align-items: center;\n  justify-content: center;\n  padding: var(--spacing-5xl, 48px) var(--spacing-2xl, 24px);\n  text-align: center;\n}\n.empty-icon[data-v-a29f92b8] {\n  font-size: 48px;\n  color: var(--text-quaternary, #c9cdd4);\n  margin-bottom: var(--spacing-lg, 16px);\n}\n.empty-title[data-v-a29f92b8] {\n  font-size: var(--font-size-lg, 16px);\n  font-weight: var(--font-weight-medium, 500);\n  color: var(--text-secondary, #4e5969);\n  margin-bottom: var(--spacing-sm, 8px);\n}\n.empty-description[data-v-a29f92b8] {\n  font-size: var(--font-size-sm, 12px);\n  color: var(--text-tertiary, #86909c);\n  max-width: 400px;\n  margin: 0;\n}\n.empty-action[data-v-a29f92b8] {\n  margin-top: var(--spacing-lg, 16px);\n}\n\n.error-container[data-v-c6d5c909] {\n  display: flex;\n  flex-direction: column;\n  align-items: center;\n  justify-content: center;\n  padding: var(--spacing-4xl, 40px) var(--spacing-2xl, 24px);\n  text-align: center;\n}\n.error-icon[data-v-c6d5c909] {\n  font-size: 48px;\n  color: var(--error, #ef4444);\n  margin-bottom: var(--spacing-lg, 16px);\n}\n.error-title[data-v-c6d5c909] {\n  font-size: var(--font-size-lg, 16px);\n  font-weight: var(--font-weight-semibold, 600);\n  color: var(--error, #ef4444);\n  margin-bottom: var(--spacing-sm, 8px);\n}\n.error-message[data-v-c6d5c909] {\n  font-size: var(--font-size-sm, 12px);\n  color: var(--text-secondary, #4e5969);\n  max-width: 500px;\n  margin: 0 0 var(--spacing-lg, 16px);\n}\n.error-action[data-v-c6d5c909] {\n  margin-top: var(--spacing-lg, 16px);\n}\n.error-retry-btn[data-v-c6d5c909] {\n  padding: var(--spacing-sm, 8px) var(--spacing-lg, 16px);\n  font-size: var(--font-size-sm, 12px);\n  color: white;\n  background-color: var(--primary, #6366f1);\n  border: none;\n  border-radius: var(--radius-md, 8px);\n  cursor: pointer;\n  transition: opacity var(--transition-fast, 0.15s) ease;\n}\n.error-retry-btn[data-v-c6d5c909]:hover {\n  opacity: 0.8;\n}\n.error-retry-btn[data-v-c6d5c909]:active {\n  opacity: 0.6;\n}\n\n.plugin-admin-layout[data-v-fb377eb7] {\n  width: 100%;\n  height: 100%;\n}\n\n.plugin-submission-detail[data-v-a7d31ce5] {\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n}\n.detail-card[data-v-a7d31ce5] {\n  margin-bottom: 0;\n}\n.description-content[data-v-a7d31ce5] {\n  color: #666;\n  line-height: 1.6;\n  white-space: pre-wrap;\n  word-break: break-word;\n}\n.icon-preview {\n&[data-v-a7d31ce5] {\n  display: flex;\n  justify-content: center;\n  align-items: center;\n  padding: 20px;\n  }\nimg[data-v-a7d31ce5] {\n    max-width: 200px;\n    max-height: 200px;\n    object-fit: contain;\n}\n}\n.empty-text[data-v-a7d31ce5] {\n  color: #999;\n  font-size: 14px;\n}\n.action-buttons[data-v-a7d31ce5] {\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n\n.plugin-submissions-page[data-v-50381363] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-50381363] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-50381363] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-50381363] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n  display: flex;\n  flex-direction: column;\n}\n.pagination-container[data-v-50381363] {\n  display: flex;\n  justify-content: flex-end;\n  margin-top: 16px;\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n\n.plugin-detail[data-v-3e715355] {\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n}\n.detail-card[data-v-3e715355] {\n  margin-bottom: 0;\n}\n.description-content[data-v-3e715355] {\n  color: #666;\n  line-height: 1.6;\n  white-space: pre-wrap;\n  word-break: break-word;\n}\n.version-list[data-v-3e715355] {\n  display: flex;\n  flex-direction: column;\n  gap: 12px;\n}\n.version-item[data-v-3e715355] {\n  padding: 12px;\n  background: #fafafa;\n  border-radius: 4px;\n  border: 1px solid #f0f0f0;\n}\n.version-header[data-v-3e715355] {\n  display: flex;\n  justify-content: space-between;\n  align-items: center;\n  margin-bottom: 8px;\n}\n.version-info[data-v-3e715355] {\n  display: flex;\n  align-items: center;\n}\n.version-number[data-v-3e715355] {\n  font-size: 16px;\n  font-weight: 600;\n  color: #333;\n}\n.version-time[data-v-3e715355] {\n  font-size: 13px;\n  color: #999;\n}\n.version-notes[data-v-3e715355] {\n  margin-bottom: 8px;\n  padding: 8px;\n  background: white;\n  border-radius: 4px;\n  font-size: 14px;\n  color: #666;\n  line-height: 1.5;\n}\n.version-meta[data-v-3e715355] {\n  font-size: 13px;\n  color: #999;\n}\n.icon-preview {\n&[data-v-3e715355] {\n  display: flex;\n  justify-content: center;\n  align-items: center;\n  padding: 20px;\n  }\nimg[data-v-3e715355] {\n    max-width: 200px;\n    max-height: 200px;\n    object-fit: contain;\n}\n}\n.empty-text[data-v-3e715355] {\n  color: #999;\n  font-size: 14px;\n}\n.rating-text[data-v-3e715355] {\n  margin-left: 8px;\n  color: #666;\n  font-size: 14px;\n}\n.action-buttons[data-v-3e715355] {\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n\n.plugin-management-page[data-v-f7880059] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-f7880059] {\n  /* 页面头部撑满宽度，无 padding */\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-f7880059] {\n  /* 内容区域有 padding */\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-f7880059] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n  display: flex;\n  flex-direction: column;\n}\n.pagination-container[data-v-f7880059] {\n  display: flex;\n  justify-content: flex-end;\n  margin-top: 16px;\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n\n.statistics-analysis-page[data-v-cb00e5b0] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-cb00e5b0] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-cb00e5b0] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.filter-card {\n&[data-v-cb00e5b0] {\n  margin-bottom: 24px;\n  }\n.filter-label[data-v-cb00e5b0] {\n    font-weight: 500;\n    color: var(--n-text-color);\n}\n}\n.overview-cards {\n&[data-v-cb00e5b0] {\n  display: grid;\n  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));\n  gap: 16px;\n  margin-bottom: 24px;\n  }\n.stat-card {\n.stat-content {\n&[data-v-cb00e5b0] {\n      display: flex;\n      align-items: center;\n      gap: 16px;\n      }\n.stat-icon[data-v-cb00e5b0] {\n        width: 64px;\n        height: 64px;\n        border-radius: 12px;\n        display: flex;\n        align-items: center;\n        justify-content: center;\n        color: white;\n}\n.stat-info {\n&[data-v-cb00e5b0] {\n        flex: 1;\n        }\n.stat-label[data-v-cb00e5b0] {\n          font-size: 14px;\n          color: var(--n-text-color-3);\n          margin-bottom: 8px;\n}\n.stat-value[data-v-cb00e5b0] {\n          font-size: 28px;\n          font-weight: 600;\n          color: var(--n-text-color);\n}\n}\n}\n}\n}\n.today-stats {\n&[data-v-cb00e5b0] {\n  margin-bottom: 24px;\n  }\n.stat-unit[data-v-cb00e5b0] {\n    font-size: 14px;\n    color: var(--n-text-color-3);\n    margin-left: 4px;\n}\n}\n.trend-charts {\n&[data-v-cb00e5b0] {\n  display: grid;\n  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));\n  gap: 16px;\n  margin-bottom: 24px;\n  }\n.chart-card {\n.chart-container {\n&[data-v-cb00e5b0] {\n      height: 300px;\n      padding: 20px 0;\n      position: relative;\n      }\n.simple-chart {\n&[data-v-cb00e5b0] {\n        display: flex;\n        align-items: flex-end;\n        justify-content: space-around;\n        height: 100%;\n        gap: 8px;\n        }\n.chart-bar {\n&[data-v-cb00e5b0] {\n          flex: 1;\n          background: linear-gradient(180deg, #2080f0 0%, #4098fc 100%);\n          border-radius: 4px 4px 0 0;\n          min-height: 20px;\n          position: relative;\n          cursor: pointer;\n          transition: all 0.3s;\n          }\n&[data-v-cb00e5b0]:hover {\n            opacity: 0.8;\n            transform: translateY(-2px);\n}\n.bar-value[data-v-cb00e5b0] {\n            position: absolute;\n            top: -24px;\n            left: 50%;\n            transform: translateX(-50%);\n            font-size: 12px;\n            color: var(--n-text-color);\n            white-space: nowrap;\n}\n}\n}\n.empty-chart[data-v-cb00e5b0] {\n        display: flex;\n        align-items: center;\n        justify-content: center;\n        height: 100%;\n        color: var(--n-text-color-3);\n        font-size: 14px;\n}\n}\n.chart-labels {\n&[data-v-cb00e5b0] {\n      display: flex;\n      justify-content: space-around;\n      margin-top: 12px;\n      padding: 0 4px;\n      }\n.chart-label[data-v-cb00e5b0] {\n        font-size: 12px;\n        color: var(--n-text-color-3);\n        text-align: center;\n        flex: 1;\n}\n}\n}\n}\n.top-plugins {\n.rank-badge[data-v-cb00e5b0] {\n    display: flex;\n    align-items: center;\n}\n}\n\n.category-management-page[data-v-cb59b69e] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-cb59b69e] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-cb59b69e] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-cb59b69e] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-cb59b69e] {\n  margin-top: 16px;\n}\n.category-detail[data-v-cb59b69e] {\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n}\n.detail-section[data-v-cb59b69e] {\n  margin-bottom: 16px;\n}\n\n.tag-management-page[data-v-cacd4f23] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-cacd4f23] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-cacd4f23] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-cacd4f23] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n}\n.tag-detail[data-v-cacd4f23] {\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n}\n.detail-section[data-v-cacd4f23] {\n  margin-bottom: 16px;\n}\n\n.developer-management-page[data-v-70519d49] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-70519d49] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-70519d49] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-70519d49] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n  display: flex;\n  flex-direction: column;\n}\n.pagination-container[data-v-70519d49] {\n  display: flex;\n  justify-content: flex-end;\n  margin-top: 16px;\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n.developer-detail[data-v-70519d49] {\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n}\n.detail-section[data-v-70519d49] {\n  margin-bottom: 16px;\n}\n.text-secondary[data-v-70519d49] {\n  color: #999;\n  font-size: 12px;\n}\n\n.audit-log-page[data-v-687b2df6] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-687b2df6] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-687b2df6] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-687b2df6] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n  display: flex;\n  flex-direction: column;\n}\n.pagination-container[data-v-687b2df6] {\n  display: flex;\n  justify-content: flex-end;\n  margin-top: 16px;\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n.log-detail[data-v-687b2df6] {\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n}\n.detail-section[data-v-687b2df6] {\n  margin-bottom: 16px;\n}\n\n.review-rules-page[data-v-0ef816e9] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-0ef816e9] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-0ef816e9] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-0ef816e9] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n  display: flex;\n  flex-direction: column;\n}\n.pagination-container[data-v-0ef816e9] {\n  display: flex;\n  justify-content: flex-end;\n  margin-top: 16px;\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n.conditions-container[data-v-0ef816e9],\n.actions-container[data-v-0ef816e9] {\n  width: 100%;\n}\n.condition-item[data-v-0ef816e9],\n.action-item[data-v-0ef816e9] {\n  margin-bottom: 8px;\n}\n\n.feedback-management-page[data-v-bf346ffe] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-bf346ffe] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-bf346ffe] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-bf346ffe] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n  display: flex;\n  flex-direction: column;\n}\n.pagination-container[data-v-bf346ffe] {\n  display: flex;\n  justify-content: flex-end;\n  margin-top: 16px;\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n.feedback-detail[data-v-bf346ffe] {\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n}\n.detail-section[data-v-bf346ffe] {\n  margin-bottom: 16px;\n}\n.feedback-content[data-v-bf346ffe],\n.handle-comment[data-v-bf346ffe] {\n  white-space: pre-wrap;\n  word-break: break-word;\n  line-height: 1.6;\n  padding: 12px;\n  background: #f5f5f5;\n  border-radius: 4px;\n}\n\n.permission-request-review-page[data-v-eb0bd909] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-eb0bd909] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-eb0bd909] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-eb0bd909] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n  display: flex;\n  flex-direction: column;\n}\n.pagination-container[data-v-eb0bd909] {\n  display: flex;\n  justify-content: flex-end;\n  margin-top: 16px;\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n\n.plugin-table-permission-management-page[data-v-d2eaf243] {\n  display: flex;\n  flex-direction: column;\n  height: 100%;\n  background: #f5f5f5;\n}\n.page-header-wrapper[data-v-d2eaf243] {\n  background: white;\n  border-bottom: 1px solid #e8e8e8;\n}\n.page-content[data-v-d2eaf243] {\n  flex: 1;\n  display: flex;\n  flex-direction: column;\n  gap: 16px;\n  padding: 16px;\n  overflow: auto;\n}\n.table-container[data-v-d2eaf243] {\n  flex: 1;\n  background: white;\n  border-radius: 8px;\n  padding: 16px;\n  overflow: auto;\n  display: flex;\n  flex-direction: column;\n}\n.pagination-container[data-v-d2eaf243] {\n  display: flex;\n  justify-content: flex-end;\n  margin-top: 16px;\n  padding-top: 16px;\n  border-top: 1px solid #f0f0f0;\n}\n/*$vite$:1*/';
  document.head.appendChild(__vite_style__);
  const _hoisted_1$d = { class: "plugin-admin-layout" };
  const _sfc_main$d = /* @__PURE__ */ vue.defineComponent({
    __name: "PluginAdminLayout",
    setup(__props) {
      return (_ctx, _cache) => {
        const _component_router_view = vue.resolveComponent("router-view");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$d, [
          vue.createVNode(_component_router_view)
        ]);
      };
    }
  });
  const _export_sfc = (sfc, props) => {
    const target = sfc.__vccOpts || sfc;
    for (const [key, val] of props) {
      target[key] = val;
    }
    return target;
  };
  const PluginAdminLayout = /* @__PURE__ */ _export_sfc(_sfc_main$d, [["__scopeId", "data-v-fb377eb7"]]);
  function useIcon(iconName) {
    const Ionicons5 = window.Ionicons5;
    if (!Ionicons5) {
      console.warn("[useIcon] Ionicons5 not found on window");
      return null;
    }
    const icon = Ionicons5[iconName];
    if (!icon) {
      console.warn(`[useIcon] Icon not found: ${iconName}`);
      return null;
    }
    return icon;
  }
  function getHostBridge() {
    if (typeof window === "undefined") {
      throw new Error("[useHostBridge] Window is not defined");
    }
    const bridge = window.GressBridge;
    if (!bridge) {
      throw new Error("[useHostBridge] GressBridge is not available. Make sure the plugin is loaded in the host application.");
    }
    return bridge;
  }
  function createMessageApi(bridge) {
    const naiveUI = window.NaiveUI;
    if (naiveUI && naiveUI.useMessage) {
      return {
        success: (content, options) => {
          bridge.notification.success(content, options);
        },
        error: (content, options) => {
          bridge.notification.error(content, options);
        },
        warning: (content, options) => {
          bridge.notification.warning(content, options);
        },
        info: (content, options) => {
          bridge.notification.info(content, options);
        },
        loading: (content, options) => {
          bridge.notification.info(content, { ...options, duration: 0 });
        }
      };
    }
    return {
      success: (content, options) => {
        bridge.notification.success(content, options);
      },
      error: (content, options) => {
        bridge.notification.error(content, options);
      },
      warning: (content, options) => {
        bridge.notification.warning(content, options);
      },
      info: (content, options) => {
        bridge.notification.info(content, options);
      },
      loading: (content, options) => {
        bridge.notification.info(content, { ...options, duration: 0 });
      }
    };
  }
  function createDialogApi(_bridge) {
    const naiveUI = window.NaiveUI;
    if (naiveUI && naiveUI.useDialog) {
      return {
        success: (options) => {
          window.alert(options.content || options.title || "Success");
        },
        error: (options) => {
          window.alert(options.content || options.title || "Error");
        },
        warning: (options) => {
          if (window.confirm(options.content || options.title || "Warning")) {
            const callback = options.onPositiveClick || options.positiveClick;
            callback == null ? void 0 : callback();
          } else {
            const callback = options.onNegativeClick || options.negativeClick;
            callback == null ? void 0 : callback();
          }
        },
        info: (options) => {
          window.alert(options.content || options.title || "Info");
        },
        create: (options) => {
          const result = window.confirm(options.content || options.title || "");
          if (result) {
            const callback = options.onPositiveClick || options.positiveClick;
            callback == null ? void 0 : callback();
          } else {
            const callback = options.onNegativeClick || options.negativeClick;
            callback == null ? void 0 : callback();
          }
          return {
            destroy: () => {
            }
          };
        }
      };
    }
    return {
      success: (options) => {
        window.alert(options.content || options.title || "Success");
      },
      error: (options) => {
        window.alert(options.content || options.title || "Error");
      },
      warning: (options) => {
        if (window.confirm(options.content || options.title || "Warning")) {
          const callback = options.onPositiveClick || options.positiveClick;
          callback == null ? void 0 : callback();
        } else {
          const callback = options.onNegativeClick || options.negativeClick;
          callback == null ? void 0 : callback();
        }
      },
      info: (options) => {
        window.alert(options.content || options.title || "Info");
      },
      create: (options) => {
        const result = window.confirm(options.content || options.title || "");
        if (result) {
          const callback = options.onPositiveClick || options.positiveClick;
          callback == null ? void 0 : callback();
        } else {
          const callback = options.onNegativeClick || options.negativeClick;
          callback == null ? void 0 : callback();
        }
        return {
          destroy: () => {
          }
        };
      }
    };
  }
  function createNotificationApi(bridge) {
    return {
      success: (options) => {
        bridge.notification.success(options.content || options.title, options);
      },
      error: (options) => {
        bridge.notification.error(options.content || options.title, options);
      },
      warning: (options) => {
        bridge.notification.warning(options.content || options.title, options);
      },
      info: (options) => {
        bridge.notification.info(options.content || options.title, options);
      },
      create: (options) => {
        const type = options.type || "info";
        bridge.notification[type](options.content || options.title, options);
        return {
          destroy: () => {
          }
        };
      }
    };
  }
  function createLoadingBarApi(_bridge) {
    let loadingElement = null;
    return {
      start: () => {
        if (!loadingElement) {
          loadingElement = document.createElement("div");
          loadingElement.style.cssText = `
          position: fixed;
          top: 0;
          left: 0;
          right: 0;
          height: 2px;
          background: #18a058;
          z-index: 9999;
          transition: width 0.3s;
        `;
          document.body.appendChild(loadingElement);
        }
        loadingElement.style.width = "70%";
      },
      finish: () => {
        if (loadingElement) {
          loadingElement.style.width = "100%";
          setTimeout(() => {
            loadingElement == null ? void 0 : loadingElement.remove();
            loadingElement = null;
          }, 300);
        }
      },
      error: () => {
        if (loadingElement) {
          loadingElement.style.background = "#d03050";
          loadingElement.style.width = "100%";
          setTimeout(() => {
            loadingElement == null ? void 0 : loadingElement.remove();
            loadingElement = null;
          }, 300);
        }
      }
    };
  }
  function useHostBridge() {
    const bridge = getHostBridge();
    return {
      bridge,
      router: bridge.router,
      route: bridge.router.currentRoute,
      message: createMessageApi(bridge),
      dialog: createDialogApi(),
      notification: createNotificationApi(bridge),
      loadingBar: createLoadingBarApi(),
      http: bridge.http,
      events: bridge.events,
      utils: bridge.utils
    };
  }
  const useMessage = () => useHostBridge().message;
  const _hoisted_1$c = { class: "plugin-submission-detail" };
  const _hoisted_2$c = { class: "description-content" };
  const _hoisted_3$c = { class: "icon-preview" };
  const _hoisted_4$c = ["src"];
  const _hoisted_5$c = {
    key: 1,
    class: "empty-text"
  };
  const _hoisted_6$7 = {
    key: 2,
    class: "action-buttons"
  };
  const _sfc_main$c = /* @__PURE__ */ vue.defineComponent({
    __name: "PluginSubmissionDetail",
    props: {
      submission: {}
    },
    emits: ["approve", "reject", "close"],
    setup(__props) {
      const Close = useIcon("CloseOutline");
      const CheckmarkDone = useIcon("CheckmarkDoneOutline");
      function getPluginTypeTag(type) {
        const typeMap = {
          TASK: { label: "任务节点", type: "info" },
          TRIGGER: { label: "触发器", type: "success" },
          APPLICATION: { label: "应用插件", type: "warning" }
        };
        return typeMap[type];
      }
      function getStatusTag(status) {
        const statusMap = {
          PENDING: { label: "待审核", type: "default" },
          APPROVED: { label: "已批准", type: "success" },
          REJECTED: { label: "已拒绝", type: "error" }
        };
        return statusMap[status];
      }
      function formatDateTime(dateStr) {
        if (!dateStr) return "-";
        const date = new Date(dateStr);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit"
        });
      }
      return (_ctx, _cache) => {
        const _component_n_descriptions_item = vue.resolveComponent("n-descriptions-item");
        const _component_n_tag = vue.resolveComponent("n-tag");
        const _component_n_descriptions = vue.resolveComponent("n-descriptions");
        const _component_n_card = vue.resolveComponent("n-card");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_n_icon = vue.resolveComponent("n-icon");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$c, [
          vue.createVNode(_component_n_card, {
            title: "基本信息",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_descriptions, {
                column: 2,
                "label-placement": "left"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_descriptions_item, { label: "插件名称" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.submission.pluginName), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "插件ID" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.submission.pluginId), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "版本号" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.submission.version), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "插件类型" }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_tag, {
                        type: getPluginTypeTag(__props.submission.pluginType).type,
                        size: "small"
                      }, {
                        default: vue.withCtx(() => [
                          vue.createTextVNode(vue.toDisplayString(getPluginTypeTag(__props.submission.pluginType).label), 1)
                        ]),
                        _: 1
                      }, 8, ["type"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "开发者" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.submission.developerName || __props.submission.developerId), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "审核状态" }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_tag, {
                        type: getStatusTag(__props.submission.status).type,
                        size: "small"
                      }, {
                        default: vue.withCtx(() => [
                          vue.createTextVNode(vue.toDisplayString(getStatusTag(__props.submission.status).label), 1)
                        ]),
                        _: 1
                      }, 8, ["type"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, {
                    label: "提交时间",
                    span: 2
                  }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(formatDateTime(__props.submission.submitTime)), 1)
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              })
            ]),
            _: 1
          }),
          vue.createVNode(_component_n_card, {
            title: "插件描述",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createElementVNode("div", _hoisted_2$c, vue.toDisplayString(__props.submission.description || "暂无描述"), 1)
            ]),
            _: 1
          }),
          __props.submission.icon ? (vue.openBlock(), vue.createBlock(_component_n_card, {
            key: 0,
            title: "插件图标",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createElementVNode("div", _hoisted_3$c, [
                vue.createElementVNode("img", {
                  src: __props.submission.icon,
                  alt: "插件图标"
                }, null, 8, _hoisted_4$c)
              ])
            ]),
            _: 1
          })) : vue.createCommentVNode("", true),
          vue.createVNode(_component_n_card, {
            title: "分类和标签",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_space, null, {
                default: vue.withCtx(() => [
                  __props.submission.category ? (vue.openBlock(), vue.createBlock(_component_n_tag, {
                    key: 0,
                    type: "info"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(" 分类: " + vue.toDisplayString(__props.submission.category), 1)
                    ]),
                    _: 1
                  })) : vue.createCommentVNode("", true),
                  (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(__props.submission.tags, (tag) => {
                    return vue.openBlock(), vue.createBlock(_component_n_tag, {
                      key: tag,
                      type: "default"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createTextVNode(vue.toDisplayString(tag), 1)
                      ]),
                      _: 2
                    }, 1024);
                  }), 128)),
                  !__props.submission.category && (!__props.submission.tags || __props.submission.tags.length === 0) ? (vue.openBlock(), vue.createElementBlock("span", _hoisted_5$c, " 暂无分类和标签 ")) : vue.createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            _: 1
          }),
          __props.submission.reviewTime ? (vue.openBlock(), vue.createBlock(_component_n_card, {
            key: 1,
            title: "审核信息",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_descriptions, {
                column: 1,
                "label-placement": "left"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_descriptions_item, { label: "审核人" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.submission.reviewerName || __props.submission.reviewerId), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "审核时间" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(formatDateTime(__props.submission.reviewTime)), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "审核意见" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.submission.reviewComment || "无"), 1)
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              })
            ]),
            _: 1
          })) : vue.createCommentVNode("", true),
          __props.submission.status === "PENDING" ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_6$7, [
            vue.createVNode(_component_n_space, { justify: "end" }, {
              default: vue.withCtx(() => [
                vue.createVNode(_component_n_button, {
                  onClick: _cache[0] || (_cache[0] = ($event) => _ctx.$emit("close"))
                }, {
                  default: vue.withCtx(() => [..._cache[3] || (_cache[3] = [
                    vue.createTextVNode(" 取消 ", -1)
                  ])]),
                  _: 1
                }),
                vue.createVNode(_component_n_button, {
                  type: "error",
                  onClick: _cache[1] || (_cache[1] = ($event) => _ctx.$emit("reject", __props.submission.id))
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Close))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[4] || (_cache[4] = vue.createTextVNode(" 拒绝 ", -1))
                  ]),
                  _: 1
                }),
                vue.createVNode(_component_n_button, {
                  type: "success",
                  onClick: _cache[2] || (_cache[2] = ($event) => _ctx.$emit("approve", __props.submission.id))
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(CheckmarkDone))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[5] || (_cache[5] = vue.createTextVNode(" 批准 ", -1))
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ])) : vue.createCommentVNode("", true)
        ]);
      };
    }
  });
  const PluginSubmissionDetail = /* @__PURE__ */ _export_sfc(_sfc_main$c, [["__scopeId", "data-v-a7d31ce5"]]);
  function bind(fn, thisArg) {
    return function wrap() {
      return fn.apply(thisArg, arguments);
    };
  }
  const { toString } = Object.prototype;
  const { getPrototypeOf } = Object;
  const { iterator, toStringTag } = Symbol;
  const kindOf = /* @__PURE__ */ ((cache) => (thing) => {
    const str = toString.call(thing);
    return cache[str] || (cache[str] = str.slice(8, -1).toLowerCase());
  })(/* @__PURE__ */ Object.create(null));
  const kindOfTest = (type) => {
    type = type.toLowerCase();
    return (thing) => kindOf(thing) === type;
  };
  const typeOfTest = (type) => (thing) => typeof thing === type;
  const { isArray } = Array;
  const isUndefined = typeOfTest("undefined");
  function isBuffer(val) {
    return val !== null && !isUndefined(val) && val.constructor !== null && !isUndefined(val.constructor) && isFunction$1(val.constructor.isBuffer) && val.constructor.isBuffer(val);
  }
  const isArrayBuffer = kindOfTest("ArrayBuffer");
  function isArrayBufferView(val) {
    let result;
    if (typeof ArrayBuffer !== "undefined" && ArrayBuffer.isView) {
      result = ArrayBuffer.isView(val);
    } else {
      result = val && val.buffer && isArrayBuffer(val.buffer);
    }
    return result;
  }
  const isString = typeOfTest("string");
  const isFunction$1 = typeOfTest("function");
  const isNumber = typeOfTest("number");
  const isObject = (thing) => thing !== null && typeof thing === "object";
  const isBoolean = (thing) => thing === true || thing === false;
  const isPlainObject = (val) => {
    if (kindOf(val) !== "object") {
      return false;
    }
    const prototype2 = getPrototypeOf(val);
    return (prototype2 === null || prototype2 === Object.prototype || Object.getPrototypeOf(prototype2) === null) && !(toStringTag in val) && !(iterator in val);
  };
  const isEmptyObject = (val) => {
    if (!isObject(val) || isBuffer(val)) {
      return false;
    }
    try {
      return Object.keys(val).length === 0 && Object.getPrototypeOf(val) === Object.prototype;
    } catch (e) {
      return false;
    }
  };
  const isDate = kindOfTest("Date");
  const isFile = kindOfTest("File");
  const isReactNativeBlob = (value) => {
    return !!(value && typeof value.uri !== "undefined");
  };
  const isReactNative = (formData) => formData && typeof formData.getParts !== "undefined";
  const isBlob = kindOfTest("Blob");
  const isFileList = kindOfTest("FileList");
  const isStream = (val) => isObject(val) && isFunction$1(val.pipe);
  function getGlobal() {
    if (typeof globalThis !== "undefined") return globalThis;
    if (typeof self !== "undefined") return self;
    if (typeof window !== "undefined") return window;
    if (typeof global !== "undefined") return global;
    return {};
  }
  const G = getGlobal();
  const FormDataCtor = typeof G.FormData !== "undefined" ? G.FormData : void 0;
  const isFormData = (thing) => {
    let kind;
    return thing && (FormDataCtor && thing instanceof FormDataCtor || isFunction$1(thing.append) && ((kind = kindOf(thing)) === "formdata" || // detect form-data instance
    kind === "object" && isFunction$1(thing.toString) && thing.toString() === "[object FormData]"));
  };
  const isURLSearchParams = kindOfTest("URLSearchParams");
  const [isReadableStream, isRequest, isResponse, isHeaders] = [
    "ReadableStream",
    "Request",
    "Response",
    "Headers"
  ].map(kindOfTest);
  const trim = (str) => {
    return str.trim ? str.trim() : str.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "");
  };
  function forEach(obj, fn, { allOwnKeys = false } = {}) {
    if (obj === null || typeof obj === "undefined") {
      return;
    }
    let i;
    let l;
    if (typeof obj !== "object") {
      obj = [obj];
    }
    if (isArray(obj)) {
      for (i = 0, l = obj.length; i < l; i++) {
        fn.call(null, obj[i], i, obj);
      }
    } else {
      if (isBuffer(obj)) {
        return;
      }
      const keys = allOwnKeys ? Object.getOwnPropertyNames(obj) : Object.keys(obj);
      const len = keys.length;
      let key;
      for (i = 0; i < len; i++) {
        key = keys[i];
        fn.call(null, obj[key], key, obj);
      }
    }
  }
  function findKey(obj, key) {
    if (isBuffer(obj)) {
      return null;
    }
    key = key.toLowerCase();
    const keys = Object.keys(obj);
    let i = keys.length;
    let _key;
    while (i-- > 0) {
      _key = keys[i];
      if (key === _key.toLowerCase()) {
        return _key;
      }
    }
    return null;
  }
  const _global = (() => {
    if (typeof globalThis !== "undefined") return globalThis;
    return typeof self !== "undefined" ? self : typeof window !== "undefined" ? window : global;
  })();
  const isContextDefined = (context) => !isUndefined(context) && context !== _global;
  function merge() {
    const { caseless, skipUndefined } = isContextDefined(this) && this || {};
    const result = {};
    const assignValue = (val, key) => {
      if (key === "__proto__" || key === "constructor" || key === "prototype") {
        return;
      }
      const targetKey = caseless && findKey(result, key) || key;
      if (isPlainObject(result[targetKey]) && isPlainObject(val)) {
        result[targetKey] = merge(result[targetKey], val);
      } else if (isPlainObject(val)) {
        result[targetKey] = merge({}, val);
      } else if (isArray(val)) {
        result[targetKey] = val.slice();
      } else if (!skipUndefined || !isUndefined(val)) {
        result[targetKey] = val;
      }
    };
    for (let i = 0, l = arguments.length; i < l; i++) {
      arguments[i] && forEach(arguments[i], assignValue);
    }
    return result;
  }
  const extend = (a, b, thisArg, { allOwnKeys } = {}) => {
    forEach(
      b,
      (val, key) => {
        if (thisArg && isFunction$1(val)) {
          Object.defineProperty(a, key, {
            value: bind(val, thisArg),
            writable: true,
            enumerable: true,
            configurable: true
          });
        } else {
          Object.defineProperty(a, key, {
            value: val,
            writable: true,
            enumerable: true,
            configurable: true
          });
        }
      },
      { allOwnKeys }
    );
    return a;
  };
  const stripBOM = (content) => {
    if (content.charCodeAt(0) === 65279) {
      content = content.slice(1);
    }
    return content;
  };
  const inherits = (constructor, superConstructor, props, descriptors) => {
    constructor.prototype = Object.create(superConstructor.prototype, descriptors);
    Object.defineProperty(constructor.prototype, "constructor", {
      value: constructor,
      writable: true,
      enumerable: false,
      configurable: true
    });
    Object.defineProperty(constructor, "super", {
      value: superConstructor.prototype
    });
    props && Object.assign(constructor.prototype, props);
  };
  const toFlatObject = (sourceObj, destObj, filter, propFilter) => {
    let props;
    let i;
    let prop;
    const merged = {};
    destObj = destObj || {};
    if (sourceObj == null) return destObj;
    do {
      props = Object.getOwnPropertyNames(sourceObj);
      i = props.length;
      while (i-- > 0) {
        prop = props[i];
        if ((!propFilter || propFilter(prop, sourceObj, destObj)) && !merged[prop]) {
          destObj[prop] = sourceObj[prop];
          merged[prop] = true;
        }
      }
      sourceObj = filter !== false && getPrototypeOf(sourceObj);
    } while (sourceObj && (!filter || filter(sourceObj, destObj)) && sourceObj !== Object.prototype);
    return destObj;
  };
  const endsWith = (str, searchString, position) => {
    str = String(str);
    if (position === void 0 || position > str.length) {
      position = str.length;
    }
    position -= searchString.length;
    const lastIndex = str.indexOf(searchString, position);
    return lastIndex !== -1 && lastIndex === position;
  };
  const toArray = (thing) => {
    if (!thing) return null;
    if (isArray(thing)) return thing;
    let i = thing.length;
    if (!isNumber(i)) return null;
    const arr = new Array(i);
    while (i-- > 0) {
      arr[i] = thing[i];
    }
    return arr;
  };
  const isTypedArray = /* @__PURE__ */ ((TypedArray) => {
    return (thing) => {
      return TypedArray && thing instanceof TypedArray;
    };
  })(typeof Uint8Array !== "undefined" && getPrototypeOf(Uint8Array));
  const forEachEntry = (obj, fn) => {
    const generator = obj && obj[iterator];
    const _iterator = generator.call(obj);
    let result;
    while ((result = _iterator.next()) && !result.done) {
      const pair = result.value;
      fn.call(obj, pair[0], pair[1]);
    }
  };
  const matchAll = (regExp, str) => {
    let matches;
    const arr = [];
    while ((matches = regExp.exec(str)) !== null) {
      arr.push(matches);
    }
    return arr;
  };
  const isHTMLForm = kindOfTest("HTMLFormElement");
  const toCamelCase = (str) => {
    return str.toLowerCase().replace(/[-_\s]([a-z\d])(\w*)/g, function replacer(m, p1, p2) {
      return p1.toUpperCase() + p2;
    });
  };
  const hasOwnProperty = (({ hasOwnProperty: hasOwnProperty2 }) => (obj, prop) => hasOwnProperty2.call(obj, prop))(Object.prototype);
  const isRegExp = kindOfTest("RegExp");
  const reduceDescriptors = (obj, reducer) => {
    const descriptors = Object.getOwnPropertyDescriptors(obj);
    const reducedDescriptors = {};
    forEach(descriptors, (descriptor, name) => {
      let ret;
      if ((ret = reducer(descriptor, name, obj)) !== false) {
        reducedDescriptors[name] = ret || descriptor;
      }
    });
    Object.defineProperties(obj, reducedDescriptors);
  };
  const freezeMethods = (obj) => {
    reduceDescriptors(obj, (descriptor, name) => {
      if (isFunction$1(obj) && ["arguments", "caller", "callee"].indexOf(name) !== -1) {
        return false;
      }
      const value = obj[name];
      if (!isFunction$1(value)) return;
      descriptor.enumerable = false;
      if ("writable" in descriptor) {
        descriptor.writable = false;
        return;
      }
      if (!descriptor.set) {
        descriptor.set = () => {
          throw Error("Can not rewrite read-only method '" + name + "'");
        };
      }
    });
  };
  const toObjectSet = (arrayOrString, delimiter) => {
    const obj = {};
    const define = (arr) => {
      arr.forEach((value) => {
        obj[value] = true;
      });
    };
    isArray(arrayOrString) ? define(arrayOrString) : define(String(arrayOrString).split(delimiter));
    return obj;
  };
  const noop = () => {
  };
  const toFiniteNumber = (value, defaultValue) => {
    return value != null && Number.isFinite(value = +value) ? value : defaultValue;
  };
  function isSpecCompliantForm(thing) {
    return !!(thing && isFunction$1(thing.append) && thing[toStringTag] === "FormData" && thing[iterator]);
  }
  const toJSONObject = (obj) => {
    const stack = new Array(10);
    const visit = (source, i) => {
      if (isObject(source)) {
        if (stack.indexOf(source) >= 0) {
          return;
        }
        if (isBuffer(source)) {
          return source;
        }
        if (!("toJSON" in source)) {
          stack[i] = source;
          const target = isArray(source) ? [] : {};
          forEach(source, (value, key) => {
            const reducedValue = visit(value, i + 1);
            !isUndefined(reducedValue) && (target[key] = reducedValue);
          });
          stack[i] = void 0;
          return target;
        }
      }
      return source;
    };
    return visit(obj, 0);
  };
  const isAsyncFn = kindOfTest("AsyncFunction");
  const isThenable = (thing) => thing && (isObject(thing) || isFunction$1(thing)) && isFunction$1(thing.then) && isFunction$1(thing.catch);
  const _setImmediate = ((setImmediateSupported, postMessageSupported) => {
    if (setImmediateSupported) {
      return setImmediate;
    }
    return postMessageSupported ? ((token, callbacks) => {
      _global.addEventListener(
        "message",
        ({ source, data }) => {
          if (source === _global && data === token) {
            callbacks.length && callbacks.shift()();
          }
        },
        false
      );
      return (cb) => {
        callbacks.push(cb);
        _global.postMessage(token, "*");
      };
    })(`axios@${Math.random()}`, []) : (cb) => setTimeout(cb);
  })(typeof setImmediate === "function", isFunction$1(_global.postMessage));
  const asap = typeof queueMicrotask !== "undefined" ? queueMicrotask.bind(_global) : typeof process !== "undefined" && process.nextTick || _setImmediate;
  const isIterable = (thing) => thing != null && isFunction$1(thing[iterator]);
  const utils$1 = {
    isArray,
    isArrayBuffer,
    isBuffer,
    isFormData,
    isArrayBufferView,
    isString,
    isNumber,
    isBoolean,
    isObject,
    isPlainObject,
    isEmptyObject,
    isReadableStream,
    isRequest,
    isResponse,
    isHeaders,
    isUndefined,
    isDate,
    isFile,
    isReactNativeBlob,
    isReactNative,
    isBlob,
    isRegExp,
    isFunction: isFunction$1,
    isStream,
    isURLSearchParams,
    isTypedArray,
    isFileList,
    forEach,
    merge,
    extend,
    trim,
    stripBOM,
    inherits,
    toFlatObject,
    kindOf,
    kindOfTest,
    endsWith,
    toArray,
    forEachEntry,
    matchAll,
    isHTMLForm,
    hasOwnProperty,
    hasOwnProp: hasOwnProperty,
    // an alias to avoid ESLint no-prototype-builtins detection
    reduceDescriptors,
    freezeMethods,
    toObjectSet,
    toCamelCase,
    noop,
    toFiniteNumber,
    findKey,
    global: _global,
    isContextDefined,
    isSpecCompliantForm,
    toJSONObject,
    isAsyncFn,
    isThenable,
    setImmediate: _setImmediate,
    asap,
    isIterable
  };
  let AxiosError$1 = class AxiosError2 extends Error {
    static from(error, code, config, request, response, customProps) {
      const axiosError = new AxiosError2(error.message, code || error.code, config, request, response);
      axiosError.cause = error;
      axiosError.name = error.name;
      if (error.status != null && axiosError.status == null) {
        axiosError.status = error.status;
      }
      customProps && Object.assign(axiosError, customProps);
      return axiosError;
    }
    /**
     * Create an Error with the specified message, config, error code, request and response.
     *
     * @param {string} message The error message.
     * @param {string} [code] The error code (for example, 'ECONNABORTED').
     * @param {Object} [config] The config.
     * @param {Object} [request] The request.
     * @param {Object} [response] The response.
     *
     * @returns {Error} The created error.
     */
    constructor(message, code, config, request, response) {
      super(message);
      Object.defineProperty(this, "message", {
        value: message,
        enumerable: true,
        writable: true,
        configurable: true
      });
      this.name = "AxiosError";
      this.isAxiosError = true;
      code && (this.code = code);
      config && (this.config = config);
      request && (this.request = request);
      if (response) {
        this.response = response;
        this.status = response.status;
      }
    }
    toJSON() {
      return {
        // Standard
        message: this.message,
        name: this.name,
        // Microsoft
        description: this.description,
        number: this.number,
        // Mozilla
        fileName: this.fileName,
        lineNumber: this.lineNumber,
        columnNumber: this.columnNumber,
        stack: this.stack,
        // Axios
        config: utils$1.toJSONObject(this.config),
        code: this.code,
        status: this.status
      };
    }
  };
  AxiosError$1.ERR_BAD_OPTION_VALUE = "ERR_BAD_OPTION_VALUE";
  AxiosError$1.ERR_BAD_OPTION = "ERR_BAD_OPTION";
  AxiosError$1.ECONNABORTED = "ECONNABORTED";
  AxiosError$1.ETIMEDOUT = "ETIMEDOUT";
  AxiosError$1.ERR_NETWORK = "ERR_NETWORK";
  AxiosError$1.ERR_FR_TOO_MANY_REDIRECTS = "ERR_FR_TOO_MANY_REDIRECTS";
  AxiosError$1.ERR_DEPRECATED = "ERR_DEPRECATED";
  AxiosError$1.ERR_BAD_RESPONSE = "ERR_BAD_RESPONSE";
  AxiosError$1.ERR_BAD_REQUEST = "ERR_BAD_REQUEST";
  AxiosError$1.ERR_CANCELED = "ERR_CANCELED";
  AxiosError$1.ERR_NOT_SUPPORT = "ERR_NOT_SUPPORT";
  AxiosError$1.ERR_INVALID_URL = "ERR_INVALID_URL";
  const httpAdapter = null;
  function isVisitable(thing) {
    return utils$1.isPlainObject(thing) || utils$1.isArray(thing);
  }
  function removeBrackets(key) {
    return utils$1.endsWith(key, "[]") ? key.slice(0, -2) : key;
  }
  function renderKey(path, key, dots) {
    if (!path) return key;
    return path.concat(key).map(function each(token, i) {
      token = removeBrackets(token);
      return !dots && i ? "[" + token + "]" : token;
    }).join(dots ? "." : "");
  }
  function isFlatArray(arr) {
    return utils$1.isArray(arr) && !arr.some(isVisitable);
  }
  const predicates = utils$1.toFlatObject(utils$1, {}, null, function filter(prop) {
    return /^is[A-Z]/.test(prop);
  });
  function toFormData$1(obj, formData, options) {
    if (!utils$1.isObject(obj)) {
      throw new TypeError("target must be an object");
    }
    formData = formData || new FormData();
    options = utils$1.toFlatObject(
      options,
      {
        metaTokens: true,
        dots: false,
        indexes: false
      },
      false,
      function defined(option, source) {
        return !utils$1.isUndefined(source[option]);
      }
    );
    const metaTokens = options.metaTokens;
    const visitor = options.visitor || defaultVisitor;
    const dots = options.dots;
    const indexes = options.indexes;
    const _Blob = options.Blob || typeof Blob !== "undefined" && Blob;
    const useBlob = _Blob && utils$1.isSpecCompliantForm(formData);
    if (!utils$1.isFunction(visitor)) {
      throw new TypeError("visitor must be a function");
    }
    function convertValue(value) {
      if (value === null) return "";
      if (utils$1.isDate(value)) {
        return value.toISOString();
      }
      if (utils$1.isBoolean(value)) {
        return value.toString();
      }
      if (!useBlob && utils$1.isBlob(value)) {
        throw new AxiosError$1("Blob is not supported. Use a Buffer instead.");
      }
      if (utils$1.isArrayBuffer(value) || utils$1.isTypedArray(value)) {
        return useBlob && typeof Blob === "function" ? new Blob([value]) : Buffer.from(value);
      }
      return value;
    }
    function defaultVisitor(value, key, path) {
      let arr = value;
      if (utils$1.isReactNative(formData) && utils$1.isReactNativeBlob(value)) {
        formData.append(renderKey(path, key, dots), convertValue(value));
        return false;
      }
      if (value && !path && typeof value === "object") {
        if (utils$1.endsWith(key, "{}")) {
          key = metaTokens ? key : key.slice(0, -2);
          value = JSON.stringify(value);
        } else if (utils$1.isArray(value) && isFlatArray(value) || (utils$1.isFileList(value) || utils$1.endsWith(key, "[]")) && (arr = utils$1.toArray(value))) {
          key = removeBrackets(key);
          arr.forEach(function each(el, index2) {
            !(utils$1.isUndefined(el) || el === null) && formData.append(
              // eslint-disable-next-line no-nested-ternary
              indexes === true ? renderKey([key], index2, dots) : indexes === null ? key : key + "[]",
              convertValue(el)
            );
          });
          return false;
        }
      }
      if (isVisitable(value)) {
        return true;
      }
      formData.append(renderKey(path, key, dots), convertValue(value));
      return false;
    }
    const stack = [];
    const exposedHelpers = Object.assign(predicates, {
      defaultVisitor,
      convertValue,
      isVisitable
    });
    function build(value, path) {
      if (utils$1.isUndefined(value)) return;
      if (stack.indexOf(value) !== -1) {
        throw Error("Circular reference detected in " + path.join("."));
      }
      stack.push(value);
      utils$1.forEach(value, function each(el, key) {
        const result = !(utils$1.isUndefined(el) || el === null) && visitor.call(formData, el, utils$1.isString(key) ? key.trim() : key, path, exposedHelpers);
        if (result === true) {
          build(el, path ? path.concat(key) : [key]);
        }
      });
      stack.pop();
    }
    if (!utils$1.isObject(obj)) {
      throw new TypeError("data must be an object");
    }
    build(obj);
    return formData;
  }
  function encode$1(str) {
    const charMap = {
      "!": "%21",
      "'": "%27",
      "(": "%28",
      ")": "%29",
      "~": "%7E",
      "%20": "+",
      "%00": "\0"
    };
    return encodeURIComponent(str).replace(/[!'()~]|%20|%00/g, function replacer(match) {
      return charMap[match];
    });
  }
  function AxiosURLSearchParams(params, options) {
    this._pairs = [];
    params && toFormData$1(params, this, options);
  }
  const prototype = AxiosURLSearchParams.prototype;
  prototype.append = function append(name, value) {
    this._pairs.push([name, value]);
  };
  prototype.toString = function toString2(encoder) {
    const _encode = encoder ? function(value) {
      return encoder.call(this, value, encode$1);
    } : encode$1;
    return this._pairs.map(function each(pair) {
      return _encode(pair[0]) + "=" + _encode(pair[1]);
    }, "").join("&");
  };
  function encode(val) {
    return encodeURIComponent(val).replace(/%3A/gi, ":").replace(/%24/g, "$").replace(/%2C/gi, ",").replace(/%20/g, "+");
  }
  function buildURL(url, params, options) {
    if (!params) {
      return url;
    }
    const _encode = options && options.encode || encode;
    const _options = utils$1.isFunction(options) ? {
      serialize: options
    } : options;
    const serializeFn = _options && _options.serialize;
    let serializedParams;
    if (serializeFn) {
      serializedParams = serializeFn(params, _options);
    } else {
      serializedParams = utils$1.isURLSearchParams(params) ? params.toString() : new AxiosURLSearchParams(params, _options).toString(_encode);
    }
    if (serializedParams) {
      const hashmarkIndex = url.indexOf("#");
      if (hashmarkIndex !== -1) {
        url = url.slice(0, hashmarkIndex);
      }
      url += (url.indexOf("?") === -1 ? "?" : "&") + serializedParams;
    }
    return url;
  }
  class InterceptorManager {
    constructor() {
      this.handlers = [];
    }
    /**
     * Add a new interceptor to the stack
     *
     * @param {Function} fulfilled The function to handle `then` for a `Promise`
     * @param {Function} rejected The function to handle `reject` for a `Promise`
     * @param {Object} options The options for the interceptor, synchronous and runWhen
     *
     * @return {Number} An ID used to remove interceptor later
     */
    use(fulfilled, rejected, options) {
      this.handlers.push({
        fulfilled,
        rejected,
        synchronous: options ? options.synchronous : false,
        runWhen: options ? options.runWhen : null
      });
      return this.handlers.length - 1;
    }
    /**
     * Remove an interceptor from the stack
     *
     * @param {Number} id The ID that was returned by `use`
     *
     * @returns {void}
     */
    eject(id) {
      if (this.handlers[id]) {
        this.handlers[id] = null;
      }
    }
    /**
     * Clear all interceptors from the stack
     *
     * @returns {void}
     */
    clear() {
      if (this.handlers) {
        this.handlers = [];
      }
    }
    /**
     * Iterate over all the registered interceptors
     *
     * This method is particularly useful for skipping over any
     * interceptors that may have become `null` calling `eject`.
     *
     * @param {Function} fn The function to call for each interceptor
     *
     * @returns {void}
     */
    forEach(fn) {
      utils$1.forEach(this.handlers, function forEachHandler(h) {
        if (h !== null) {
          fn(h);
        }
      });
    }
  }
  const transitionalDefaults = {
    silentJSONParsing: true,
    forcedJSONParsing: true,
    clarifyTimeoutError: false,
    legacyInterceptorReqResOrdering: true
  };
  const URLSearchParams$1 = typeof URLSearchParams !== "undefined" ? URLSearchParams : AxiosURLSearchParams;
  const FormData$1 = typeof FormData !== "undefined" ? FormData : null;
  const Blob$1 = typeof Blob !== "undefined" ? Blob : null;
  const platform$1 = {
    isBrowser: true,
    classes: {
      URLSearchParams: URLSearchParams$1,
      FormData: FormData$1,
      Blob: Blob$1
    },
    protocols: ["http", "https", "file", "blob", "url", "data"]
  };
  const hasBrowserEnv = typeof window !== "undefined" && typeof document !== "undefined";
  const _navigator = typeof navigator === "object" && navigator || void 0;
  const hasStandardBrowserEnv = hasBrowserEnv && (!_navigator || ["ReactNative", "NativeScript", "NS"].indexOf(_navigator.product) < 0);
  const hasStandardBrowserWebWorkerEnv = (() => {
    return typeof WorkerGlobalScope !== "undefined" && // eslint-disable-next-line no-undef
    self instanceof WorkerGlobalScope && typeof self.importScripts === "function";
  })();
  const origin = hasBrowserEnv && window.location.href || "http://localhost";
  const utils = /* @__PURE__ */ Object.freeze(/* @__PURE__ */ Object.defineProperty({
    __proto__: null,
    hasBrowserEnv,
    hasStandardBrowserEnv,
    hasStandardBrowserWebWorkerEnv,
    navigator: _navigator,
    origin
  }, Symbol.toStringTag, { value: "Module" }));
  const platform = {
    ...utils,
    ...platform$1
  };
  function toURLEncodedForm(data, options) {
    return toFormData$1(data, new platform.classes.URLSearchParams(), {
      visitor: function(value, key, path, helpers) {
        if (platform.isNode && utils$1.isBuffer(value)) {
          this.append(key, value.toString("base64"));
          return false;
        }
        return helpers.defaultVisitor.apply(this, arguments);
      },
      ...options
    });
  }
  function parsePropPath(name) {
    return utils$1.matchAll(/\w+|\[(\w*)]/g, name).map((match) => {
      return match[0] === "[]" ? "" : match[1] || match[0];
    });
  }
  function arrayToObject(arr) {
    const obj = {};
    const keys = Object.keys(arr);
    let i;
    const len = keys.length;
    let key;
    for (i = 0; i < len; i++) {
      key = keys[i];
      obj[key] = arr[key];
    }
    return obj;
  }
  function formDataToJSON(formData) {
    function buildPath(path, value, target, index2) {
      let name = path[index2++];
      if (name === "__proto__") return true;
      const isNumericKey = Number.isFinite(+name);
      const isLast = index2 >= path.length;
      name = !name && utils$1.isArray(target) ? target.length : name;
      if (isLast) {
        if (utils$1.hasOwnProp(target, name)) {
          target[name] = [target[name], value];
        } else {
          target[name] = value;
        }
        return !isNumericKey;
      }
      if (!target[name] || !utils$1.isObject(target[name])) {
        target[name] = [];
      }
      const result = buildPath(path, value, target[name], index2);
      if (result && utils$1.isArray(target[name])) {
        target[name] = arrayToObject(target[name]);
      }
      return !isNumericKey;
    }
    if (utils$1.isFormData(formData) && utils$1.isFunction(formData.entries)) {
      const obj = {};
      utils$1.forEachEntry(formData, (name, value) => {
        buildPath(parsePropPath(name), value, obj, 0);
      });
      return obj;
    }
    return null;
  }
  function stringifySafely(rawValue, parser, encoder) {
    if (utils$1.isString(rawValue)) {
      try {
        (parser || JSON.parse)(rawValue);
        return utils$1.trim(rawValue);
      } catch (e) {
        if (e.name !== "SyntaxError") {
          throw e;
        }
      }
    }
    return (encoder || JSON.stringify)(rawValue);
  }
  const defaults = {
    transitional: transitionalDefaults,
    adapter: ["xhr", "http", "fetch"],
    transformRequest: [
      function transformRequest(data, headers) {
        const contentType = headers.getContentType() || "";
        const hasJSONContentType = contentType.indexOf("application/json") > -1;
        const isObjectPayload = utils$1.isObject(data);
        if (isObjectPayload && utils$1.isHTMLForm(data)) {
          data = new FormData(data);
        }
        const isFormData2 = utils$1.isFormData(data);
        if (isFormData2) {
          return hasJSONContentType ? JSON.stringify(formDataToJSON(data)) : data;
        }
        if (utils$1.isArrayBuffer(data) || utils$1.isBuffer(data) || utils$1.isStream(data) || utils$1.isFile(data) || utils$1.isBlob(data) || utils$1.isReadableStream(data)) {
          return data;
        }
        if (utils$1.isArrayBufferView(data)) {
          return data.buffer;
        }
        if (utils$1.isURLSearchParams(data)) {
          headers.setContentType("application/x-www-form-urlencoded;charset=utf-8", false);
          return data.toString();
        }
        let isFileList2;
        if (isObjectPayload) {
          if (contentType.indexOf("application/x-www-form-urlencoded") > -1) {
            return toURLEncodedForm(data, this.formSerializer).toString();
          }
          if ((isFileList2 = utils$1.isFileList(data)) || contentType.indexOf("multipart/form-data") > -1) {
            const _FormData = this.env && this.env.FormData;
            return toFormData$1(
              isFileList2 ? { "files[]": data } : data,
              _FormData && new _FormData(),
              this.formSerializer
            );
          }
        }
        if (isObjectPayload || hasJSONContentType) {
          headers.setContentType("application/json", false);
          return stringifySafely(data);
        }
        return data;
      }
    ],
    transformResponse: [
      function transformResponse(data) {
        const transitional = this.transitional || defaults.transitional;
        const forcedJSONParsing = transitional && transitional.forcedJSONParsing;
        const JSONRequested = this.responseType === "json";
        if (utils$1.isResponse(data) || utils$1.isReadableStream(data)) {
          return data;
        }
        if (data && utils$1.isString(data) && (forcedJSONParsing && !this.responseType || JSONRequested)) {
          const silentJSONParsing = transitional && transitional.silentJSONParsing;
          const strictJSONParsing = !silentJSONParsing && JSONRequested;
          try {
            return JSON.parse(data, this.parseReviver);
          } catch (e) {
            if (strictJSONParsing) {
              if (e.name === "SyntaxError") {
                throw AxiosError$1.from(e, AxiosError$1.ERR_BAD_RESPONSE, this, null, this.response);
              }
              throw e;
            }
          }
        }
        return data;
      }
    ],
    /**
     * A timeout in milliseconds to abort a request. If set to 0 (default) a
     * timeout is not created.
     */
    timeout: 0,
    xsrfCookieName: "XSRF-TOKEN",
    xsrfHeaderName: "X-XSRF-TOKEN",
    maxContentLength: -1,
    maxBodyLength: -1,
    env: {
      FormData: platform.classes.FormData,
      Blob: platform.classes.Blob
    },
    validateStatus: function validateStatus(status) {
      return status >= 200 && status < 300;
    },
    headers: {
      common: {
        Accept: "application/json, text/plain, */*",
        "Content-Type": void 0
      }
    }
  };
  utils$1.forEach(["delete", "get", "head", "post", "put", "patch"], (method) => {
    defaults.headers[method] = {};
  });
  const ignoreDuplicateOf = utils$1.toObjectSet([
    "age",
    "authorization",
    "content-length",
    "content-type",
    "etag",
    "expires",
    "from",
    "host",
    "if-modified-since",
    "if-unmodified-since",
    "last-modified",
    "location",
    "max-forwards",
    "proxy-authorization",
    "referer",
    "retry-after",
    "user-agent"
  ]);
  const parseHeaders = (rawHeaders) => {
    const parsed = {};
    let key;
    let val;
    let i;
    rawHeaders && rawHeaders.split("\n").forEach(function parser(line) {
      i = line.indexOf(":");
      key = line.substring(0, i).trim().toLowerCase();
      val = line.substring(i + 1).trim();
      if (!key || parsed[key] && ignoreDuplicateOf[key]) {
        return;
      }
      if (key === "set-cookie") {
        if (parsed[key]) {
          parsed[key].push(val);
        } else {
          parsed[key] = [val];
        }
      } else {
        parsed[key] = parsed[key] ? parsed[key] + ", " + val : val;
      }
    });
    return parsed;
  };
  const $internals = Symbol("internals");
  function normalizeHeader(header) {
    return header && String(header).trim().toLowerCase();
  }
  function normalizeValue(value) {
    if (value === false || value == null) {
      return value;
    }
    return utils$1.isArray(value) ? value.map(normalizeValue) : String(value);
  }
  function parseTokens(str) {
    const tokens = /* @__PURE__ */ Object.create(null);
    const tokensRE = /([^\s,;=]+)\s*(?:=\s*([^,;]+))?/g;
    let match;
    while (match = tokensRE.exec(str)) {
      tokens[match[1]] = match[2];
    }
    return tokens;
  }
  const isValidHeaderName = (str) => /^[-_a-zA-Z0-9^`|~,!#$%&'*+.]+$/.test(str.trim());
  function matchHeaderValue(context, value, header, filter, isHeaderNameFilter) {
    if (utils$1.isFunction(filter)) {
      return filter.call(this, value, header);
    }
    if (isHeaderNameFilter) {
      value = header;
    }
    if (!utils$1.isString(value)) return;
    if (utils$1.isString(filter)) {
      return value.indexOf(filter) !== -1;
    }
    if (utils$1.isRegExp(filter)) {
      return filter.test(value);
    }
  }
  function formatHeader(header) {
    return header.trim().toLowerCase().replace(/([a-z\d])(\w*)/g, (w, char, str) => {
      return char.toUpperCase() + str;
    });
  }
  function buildAccessors(obj, header) {
    const accessorName = utils$1.toCamelCase(" " + header);
    ["get", "set", "has"].forEach((methodName) => {
      Object.defineProperty(obj, methodName + accessorName, {
        value: function(arg1, arg2, arg3) {
          return this[methodName].call(this, header, arg1, arg2, arg3);
        },
        configurable: true
      });
    });
  }
  let AxiosHeaders$1 = class AxiosHeaders {
    constructor(headers) {
      headers && this.set(headers);
    }
    set(header, valueOrRewrite, rewrite) {
      const self2 = this;
      function setHeader(_value, _header, _rewrite) {
        const lHeader = normalizeHeader(_header);
        if (!lHeader) {
          throw new Error("header name must be a non-empty string");
        }
        const key = utils$1.findKey(self2, lHeader);
        if (!key || self2[key] === void 0 || _rewrite === true || _rewrite === void 0 && self2[key] !== false) {
          self2[key || _header] = normalizeValue(_value);
        }
      }
      const setHeaders = (headers, _rewrite) => utils$1.forEach(headers, (_value, _header) => setHeader(_value, _header, _rewrite));
      if (utils$1.isPlainObject(header) || header instanceof this.constructor) {
        setHeaders(header, valueOrRewrite);
      } else if (utils$1.isString(header) && (header = header.trim()) && !isValidHeaderName(header)) {
        setHeaders(parseHeaders(header), valueOrRewrite);
      } else if (utils$1.isObject(header) && utils$1.isIterable(header)) {
        let obj = {}, dest, key;
        for (const entry of header) {
          if (!utils$1.isArray(entry)) {
            throw TypeError("Object iterator must return a key-value pair");
          }
          obj[key = entry[0]] = (dest = obj[key]) ? utils$1.isArray(dest) ? [...dest, entry[1]] : [dest, entry[1]] : entry[1];
        }
        setHeaders(obj, valueOrRewrite);
      } else {
        header != null && setHeader(valueOrRewrite, header, rewrite);
      }
      return this;
    }
    get(header, parser) {
      header = normalizeHeader(header);
      if (header) {
        const key = utils$1.findKey(this, header);
        if (key) {
          const value = this[key];
          if (!parser) {
            return value;
          }
          if (parser === true) {
            return parseTokens(value);
          }
          if (utils$1.isFunction(parser)) {
            return parser.call(this, value, key);
          }
          if (utils$1.isRegExp(parser)) {
            return parser.exec(value);
          }
          throw new TypeError("parser must be boolean|regexp|function");
        }
      }
    }
    has(header, matcher) {
      header = normalizeHeader(header);
      if (header) {
        const key = utils$1.findKey(this, header);
        return !!(key && this[key] !== void 0 && (!matcher || matchHeaderValue(this, this[key], key, matcher)));
      }
      return false;
    }
    delete(header, matcher) {
      const self2 = this;
      let deleted = false;
      function deleteHeader(_header) {
        _header = normalizeHeader(_header);
        if (_header) {
          const key = utils$1.findKey(self2, _header);
          if (key && (!matcher || matchHeaderValue(self2, self2[key], key, matcher))) {
            delete self2[key];
            deleted = true;
          }
        }
      }
      if (utils$1.isArray(header)) {
        header.forEach(deleteHeader);
      } else {
        deleteHeader(header);
      }
      return deleted;
    }
    clear(matcher) {
      const keys = Object.keys(this);
      let i = keys.length;
      let deleted = false;
      while (i--) {
        const key = keys[i];
        if (!matcher || matchHeaderValue(this, this[key], key, matcher, true)) {
          delete this[key];
          deleted = true;
        }
      }
      return deleted;
    }
    normalize(format) {
      const self2 = this;
      const headers = {};
      utils$1.forEach(this, (value, header) => {
        const key = utils$1.findKey(headers, header);
        if (key) {
          self2[key] = normalizeValue(value);
          delete self2[header];
          return;
        }
        const normalized = format ? formatHeader(header) : String(header).trim();
        if (normalized !== header) {
          delete self2[header];
        }
        self2[normalized] = normalizeValue(value);
        headers[normalized] = true;
      });
      return this;
    }
    concat(...targets) {
      return this.constructor.concat(this, ...targets);
    }
    toJSON(asStrings) {
      const obj = /* @__PURE__ */ Object.create(null);
      utils$1.forEach(this, (value, header) => {
        value != null && value !== false && (obj[header] = asStrings && utils$1.isArray(value) ? value.join(", ") : value);
      });
      return obj;
    }
    [Symbol.iterator]() {
      return Object.entries(this.toJSON())[Symbol.iterator]();
    }
    toString() {
      return Object.entries(this.toJSON()).map(([header, value]) => header + ": " + value).join("\n");
    }
    getSetCookie() {
      return this.get("set-cookie") || [];
    }
    get [Symbol.toStringTag]() {
      return "AxiosHeaders";
    }
    static from(thing) {
      return thing instanceof this ? thing : new this(thing);
    }
    static concat(first, ...targets) {
      const computed = new this(first);
      targets.forEach((target) => computed.set(target));
      return computed;
    }
    static accessor(header) {
      const internals = this[$internals] = this[$internals] = {
        accessors: {}
      };
      const accessors = internals.accessors;
      const prototype2 = this.prototype;
      function defineAccessor(_header) {
        const lHeader = normalizeHeader(_header);
        if (!accessors[lHeader]) {
          buildAccessors(prototype2, _header);
          accessors[lHeader] = true;
        }
      }
      utils$1.isArray(header) ? header.forEach(defineAccessor) : defineAccessor(header);
      return this;
    }
  };
  AxiosHeaders$1.accessor([
    "Content-Type",
    "Content-Length",
    "Accept",
    "Accept-Encoding",
    "User-Agent",
    "Authorization"
  ]);
  utils$1.reduceDescriptors(AxiosHeaders$1.prototype, ({ value }, key) => {
    let mapped = key[0].toUpperCase() + key.slice(1);
    return {
      get: () => value,
      set(headerValue) {
        this[mapped] = headerValue;
      }
    };
  });
  utils$1.freezeMethods(AxiosHeaders$1);
  function transformData(fns, response) {
    const config = this || defaults;
    const context = response || config;
    const headers = AxiosHeaders$1.from(context.headers);
    let data = context.data;
    utils$1.forEach(fns, function transform(fn) {
      data = fn.call(config, data, headers.normalize(), response ? response.status : void 0);
    });
    headers.normalize();
    return data;
  }
  function isCancel$1(value) {
    return !!(value && value.__CANCEL__);
  }
  let CanceledError$1 = class CanceledError extends AxiosError$1 {
    /**
     * A `CanceledError` is an object that is thrown when an operation is canceled.
     *
     * @param {string=} message The message.
     * @param {Object=} config The config.
     * @param {Object=} request The request.
     *
     * @returns {CanceledError} The created error.
     */
    constructor(message, config, request) {
      super(message == null ? "canceled" : message, AxiosError$1.ERR_CANCELED, config, request);
      this.name = "CanceledError";
      this.__CANCEL__ = true;
    }
  };
  function settle(resolve, reject, response) {
    const validateStatus = response.config.validateStatus;
    if (!response.status || !validateStatus || validateStatus(response.status)) {
      resolve(response);
    } else {
      reject(
        new AxiosError$1(
          "Request failed with status code " + response.status,
          [AxiosError$1.ERR_BAD_REQUEST, AxiosError$1.ERR_BAD_RESPONSE][Math.floor(response.status / 100) - 4],
          response.config,
          response.request,
          response
        )
      );
    }
  }
  function parseProtocol(url) {
    const match = /^([-+\w]{1,25})(:?\/\/|:)/.exec(url);
    return match && match[1] || "";
  }
  function speedometer(samplesCount, min) {
    samplesCount = samplesCount || 10;
    const bytes = new Array(samplesCount);
    const timestamps = new Array(samplesCount);
    let head = 0;
    let tail = 0;
    let firstSampleTS;
    min = min !== void 0 ? min : 1e3;
    return function push(chunkLength) {
      const now = Date.now();
      const startedAt = timestamps[tail];
      if (!firstSampleTS) {
        firstSampleTS = now;
      }
      bytes[head] = chunkLength;
      timestamps[head] = now;
      let i = tail;
      let bytesCount = 0;
      while (i !== head) {
        bytesCount += bytes[i++];
        i = i % samplesCount;
      }
      head = (head + 1) % samplesCount;
      if (head === tail) {
        tail = (tail + 1) % samplesCount;
      }
      if (now - firstSampleTS < min) {
        return;
      }
      const passed = startedAt && now - startedAt;
      return passed ? Math.round(bytesCount * 1e3 / passed) : void 0;
    };
  }
  function throttle(fn, freq) {
    let timestamp = 0;
    let threshold = 1e3 / freq;
    let lastArgs;
    let timer;
    const invoke = (args, now = Date.now()) => {
      timestamp = now;
      lastArgs = null;
      if (timer) {
        clearTimeout(timer);
        timer = null;
      }
      fn(...args);
    };
    const throttled = (...args) => {
      const now = Date.now();
      const passed = now - timestamp;
      if (passed >= threshold) {
        invoke(args, now);
      } else {
        lastArgs = args;
        if (!timer) {
          timer = setTimeout(() => {
            timer = null;
            invoke(lastArgs);
          }, threshold - passed);
        }
      }
    };
    const flush = () => lastArgs && invoke(lastArgs);
    return [throttled, flush];
  }
  const progressEventReducer = (listener, isDownloadStream, freq = 3) => {
    let bytesNotified = 0;
    const _speedometer = speedometer(50, 250);
    return throttle((e) => {
      const loaded = e.loaded;
      const total = e.lengthComputable ? e.total : void 0;
      const progressBytes = loaded - bytesNotified;
      const rate = _speedometer(progressBytes);
      const inRange = loaded <= total;
      bytesNotified = loaded;
      const data = {
        loaded,
        total,
        progress: total ? loaded / total : void 0,
        bytes: progressBytes,
        rate: rate ? rate : void 0,
        estimated: rate && total && inRange ? (total - loaded) / rate : void 0,
        event: e,
        lengthComputable: total != null,
        [isDownloadStream ? "download" : "upload"]: true
      };
      listener(data);
    }, freq);
  };
  const progressEventDecorator = (total, throttled) => {
    const lengthComputable = total != null;
    return [
      (loaded) => throttled[0]({
        lengthComputable,
        total,
        loaded
      }),
      throttled[1]
    ];
  };
  const asyncDecorator = (fn) => (...args) => utils$1.asap(() => fn(...args));
  const isURLSameOrigin = platform.hasStandardBrowserEnv ? /* @__PURE__ */ ((origin2, isMSIE) => (url) => {
    url = new URL(url, platform.origin);
    return origin2.protocol === url.protocol && origin2.host === url.host && (isMSIE || origin2.port === url.port);
  })(
    new URL(platform.origin),
    platform.navigator && /(msie|trident)/i.test(platform.navigator.userAgent)
  ) : () => true;
  const cookies = platform.hasStandardBrowserEnv ? (
    // Standard browser envs support document.cookie
    {
      write(name, value, expires, path, domain, secure, sameSite) {
        if (typeof document === "undefined") return;
        const cookie = [`${name}=${encodeURIComponent(value)}`];
        if (utils$1.isNumber(expires)) {
          cookie.push(`expires=${new Date(expires).toUTCString()}`);
        }
        if (utils$1.isString(path)) {
          cookie.push(`path=${path}`);
        }
        if (utils$1.isString(domain)) {
          cookie.push(`domain=${domain}`);
        }
        if (secure === true) {
          cookie.push("secure");
        }
        if (utils$1.isString(sameSite)) {
          cookie.push(`SameSite=${sameSite}`);
        }
        document.cookie = cookie.join("; ");
      },
      read(name) {
        if (typeof document === "undefined") return null;
        const match = document.cookie.match(new RegExp("(?:^|; )" + name + "=([^;]*)"));
        return match ? decodeURIComponent(match[1]) : null;
      },
      remove(name) {
        this.write(name, "", Date.now() - 864e5, "/");
      }
    }
  ) : (
    // Non-standard browser env (web workers, react-native) lack needed support.
    {
      write() {
      },
      read() {
        return null;
      },
      remove() {
      }
    }
  );
  function isAbsoluteURL(url) {
    if (typeof url !== "string") {
      return false;
    }
    return /^([a-z][a-z\d+\-.]*:)?\/\//i.test(url);
  }
  function combineURLs(baseURL, relativeURL) {
    return relativeURL ? baseURL.replace(/\/?\/$/, "") + "/" + relativeURL.replace(/^\/+/, "") : baseURL;
  }
  function buildFullPath(baseURL, requestedURL, allowAbsoluteUrls) {
    let isRelativeUrl = !isAbsoluteURL(requestedURL);
    if (baseURL && (isRelativeUrl || allowAbsoluteUrls == false)) {
      return combineURLs(baseURL, requestedURL);
    }
    return requestedURL;
  }
  const headersToObject = (thing) => thing instanceof AxiosHeaders$1 ? { ...thing } : thing;
  function mergeConfig$1(config1, config2) {
    config2 = config2 || {};
    const config = {};
    function getMergedValue(target, source, prop, caseless) {
      if (utils$1.isPlainObject(target) && utils$1.isPlainObject(source)) {
        return utils$1.merge.call({ caseless }, target, source);
      } else if (utils$1.isPlainObject(source)) {
        return utils$1.merge({}, source);
      } else if (utils$1.isArray(source)) {
        return source.slice();
      }
      return source;
    }
    function mergeDeepProperties(a, b, prop, caseless) {
      if (!utils$1.isUndefined(b)) {
        return getMergedValue(a, b, prop, caseless);
      } else if (!utils$1.isUndefined(a)) {
        return getMergedValue(void 0, a, prop, caseless);
      }
    }
    function valueFromConfig2(a, b) {
      if (!utils$1.isUndefined(b)) {
        return getMergedValue(void 0, b);
      }
    }
    function defaultToConfig2(a, b) {
      if (!utils$1.isUndefined(b)) {
        return getMergedValue(void 0, b);
      } else if (!utils$1.isUndefined(a)) {
        return getMergedValue(void 0, a);
      }
    }
    function mergeDirectKeys(a, b, prop) {
      if (prop in config2) {
        return getMergedValue(a, b);
      } else if (prop in config1) {
        return getMergedValue(void 0, a);
      }
    }
    const mergeMap = {
      url: valueFromConfig2,
      method: valueFromConfig2,
      data: valueFromConfig2,
      baseURL: defaultToConfig2,
      transformRequest: defaultToConfig2,
      transformResponse: defaultToConfig2,
      paramsSerializer: defaultToConfig2,
      timeout: defaultToConfig2,
      timeoutMessage: defaultToConfig2,
      withCredentials: defaultToConfig2,
      withXSRFToken: defaultToConfig2,
      adapter: defaultToConfig2,
      responseType: defaultToConfig2,
      xsrfCookieName: defaultToConfig2,
      xsrfHeaderName: defaultToConfig2,
      onUploadProgress: defaultToConfig2,
      onDownloadProgress: defaultToConfig2,
      decompress: defaultToConfig2,
      maxContentLength: defaultToConfig2,
      maxBodyLength: defaultToConfig2,
      beforeRedirect: defaultToConfig2,
      transport: defaultToConfig2,
      httpAgent: defaultToConfig2,
      httpsAgent: defaultToConfig2,
      cancelToken: defaultToConfig2,
      socketPath: defaultToConfig2,
      responseEncoding: defaultToConfig2,
      validateStatus: mergeDirectKeys,
      headers: (a, b, prop) => mergeDeepProperties(headersToObject(a), headersToObject(b), prop, true)
    };
    utils$1.forEach(Object.keys({ ...config1, ...config2 }), function computeConfigValue(prop) {
      if (prop === "__proto__" || prop === "constructor" || prop === "prototype") return;
      const merge2 = utils$1.hasOwnProp(mergeMap, prop) ? mergeMap[prop] : mergeDeepProperties;
      const configValue = merge2(config1[prop], config2[prop], prop);
      utils$1.isUndefined(configValue) && merge2 !== mergeDirectKeys || (config[prop] = configValue);
    });
    return config;
  }
  const resolveConfig = (config) => {
    const newConfig = mergeConfig$1({}, config);
    let { data, withXSRFToken, xsrfHeaderName, xsrfCookieName, headers, auth } = newConfig;
    newConfig.headers = headers = AxiosHeaders$1.from(headers);
    newConfig.url = buildURL(
      buildFullPath(newConfig.baseURL, newConfig.url, newConfig.allowAbsoluteUrls),
      config.params,
      config.paramsSerializer
    );
    if (auth) {
      headers.set(
        "Authorization",
        "Basic " + btoa(
          (auth.username || "") + ":" + (auth.password ? unescape(encodeURIComponent(auth.password)) : "")
        )
      );
    }
    if (utils$1.isFormData(data)) {
      if (platform.hasStandardBrowserEnv || platform.hasStandardBrowserWebWorkerEnv) {
        headers.setContentType(void 0);
      } else if (utils$1.isFunction(data.getHeaders)) {
        const formHeaders = data.getHeaders();
        const allowedHeaders = ["content-type", "content-length"];
        Object.entries(formHeaders).forEach(([key, val]) => {
          if (allowedHeaders.includes(key.toLowerCase())) {
            headers.set(key, val);
          }
        });
      }
    }
    if (platform.hasStandardBrowserEnv) {
      withXSRFToken && utils$1.isFunction(withXSRFToken) && (withXSRFToken = withXSRFToken(newConfig));
      if (withXSRFToken || withXSRFToken !== false && isURLSameOrigin(newConfig.url)) {
        const xsrfValue = xsrfHeaderName && xsrfCookieName && cookies.read(xsrfCookieName);
        if (xsrfValue) {
          headers.set(xsrfHeaderName, xsrfValue);
        }
      }
    }
    return newConfig;
  };
  const isXHRAdapterSupported = typeof XMLHttpRequest !== "undefined";
  const xhrAdapter = isXHRAdapterSupported && function(config) {
    return new Promise(function dispatchXhrRequest(resolve, reject) {
      const _config = resolveConfig(config);
      let requestData = _config.data;
      const requestHeaders = AxiosHeaders$1.from(_config.headers).normalize();
      let { responseType, onUploadProgress, onDownloadProgress } = _config;
      let onCanceled;
      let uploadThrottled, downloadThrottled;
      let flushUpload, flushDownload;
      function done() {
        flushUpload && flushUpload();
        flushDownload && flushDownload();
        _config.cancelToken && _config.cancelToken.unsubscribe(onCanceled);
        _config.signal && _config.signal.removeEventListener("abort", onCanceled);
      }
      let request = new XMLHttpRequest();
      request.open(_config.method.toUpperCase(), _config.url, true);
      request.timeout = _config.timeout;
      function onloadend() {
        if (!request) {
          return;
        }
        const responseHeaders = AxiosHeaders$1.from(
          "getAllResponseHeaders" in request && request.getAllResponseHeaders()
        );
        const responseData = !responseType || responseType === "text" || responseType === "json" ? request.responseText : request.response;
        const response = {
          data: responseData,
          status: request.status,
          statusText: request.statusText,
          headers: responseHeaders,
          config,
          request
        };
        settle(
          function _resolve(value) {
            resolve(value);
            done();
          },
          function _reject(err) {
            reject(err);
            done();
          },
          response
        );
        request = null;
      }
      if ("onloadend" in request) {
        request.onloadend = onloadend;
      } else {
        request.onreadystatechange = function handleLoad() {
          if (!request || request.readyState !== 4) {
            return;
          }
          if (request.status === 0 && !(request.responseURL && request.responseURL.indexOf("file:") === 0)) {
            return;
          }
          setTimeout(onloadend);
        };
      }
      request.onabort = function handleAbort() {
        if (!request) {
          return;
        }
        reject(new AxiosError$1("Request aborted", AxiosError$1.ECONNABORTED, config, request));
        request = null;
      };
      request.onerror = function handleError(event) {
        const msg = event && event.message ? event.message : "Network Error";
        const err = new AxiosError$1(msg, AxiosError$1.ERR_NETWORK, config, request);
        err.event = event || null;
        reject(err);
        request = null;
      };
      request.ontimeout = function handleTimeout() {
        let timeoutErrorMessage = _config.timeout ? "timeout of " + _config.timeout + "ms exceeded" : "timeout exceeded";
        const transitional = _config.transitional || transitionalDefaults;
        if (_config.timeoutErrorMessage) {
          timeoutErrorMessage = _config.timeoutErrorMessage;
        }
        reject(
          new AxiosError$1(
            timeoutErrorMessage,
            transitional.clarifyTimeoutError ? AxiosError$1.ETIMEDOUT : AxiosError$1.ECONNABORTED,
            config,
            request
          )
        );
        request = null;
      };
      requestData === void 0 && requestHeaders.setContentType(null);
      if ("setRequestHeader" in request) {
        utils$1.forEach(requestHeaders.toJSON(), function setRequestHeader(val, key) {
          request.setRequestHeader(key, val);
        });
      }
      if (!utils$1.isUndefined(_config.withCredentials)) {
        request.withCredentials = !!_config.withCredentials;
      }
      if (responseType && responseType !== "json") {
        request.responseType = _config.responseType;
      }
      if (onDownloadProgress) {
        [downloadThrottled, flushDownload] = progressEventReducer(onDownloadProgress, true);
        request.addEventListener("progress", downloadThrottled);
      }
      if (onUploadProgress && request.upload) {
        [uploadThrottled, flushUpload] = progressEventReducer(onUploadProgress);
        request.upload.addEventListener("progress", uploadThrottled);
        request.upload.addEventListener("loadend", flushUpload);
      }
      if (_config.cancelToken || _config.signal) {
        onCanceled = (cancel) => {
          if (!request) {
            return;
          }
          reject(!cancel || cancel.type ? new CanceledError$1(null, config, request) : cancel);
          request.abort();
          request = null;
        };
        _config.cancelToken && _config.cancelToken.subscribe(onCanceled);
        if (_config.signal) {
          _config.signal.aborted ? onCanceled() : _config.signal.addEventListener("abort", onCanceled);
        }
      }
      const protocol = parseProtocol(_config.url);
      if (protocol && platform.protocols.indexOf(protocol) === -1) {
        reject(
          new AxiosError$1(
            "Unsupported protocol " + protocol + ":",
            AxiosError$1.ERR_BAD_REQUEST,
            config
          )
        );
        return;
      }
      request.send(requestData || null);
    });
  };
  const composeSignals = (signals, timeout) => {
    const { length } = signals = signals ? signals.filter(Boolean) : [];
    if (timeout || length) {
      let controller = new AbortController();
      let aborted;
      const onabort = function(reason) {
        if (!aborted) {
          aborted = true;
          unsubscribe();
          const err = reason instanceof Error ? reason : this.reason;
          controller.abort(
            err instanceof AxiosError$1 ? err : new CanceledError$1(err instanceof Error ? err.message : err)
          );
        }
      };
      let timer = timeout && setTimeout(() => {
        timer = null;
        onabort(new AxiosError$1(`timeout of ${timeout}ms exceeded`, AxiosError$1.ETIMEDOUT));
      }, timeout);
      const unsubscribe = () => {
        if (signals) {
          timer && clearTimeout(timer);
          timer = null;
          signals.forEach((signal2) => {
            signal2.unsubscribe ? signal2.unsubscribe(onabort) : signal2.removeEventListener("abort", onabort);
          });
          signals = null;
        }
      };
      signals.forEach((signal2) => signal2.addEventListener("abort", onabort));
      const { signal } = controller;
      signal.unsubscribe = () => utils$1.asap(unsubscribe);
      return signal;
    }
  };
  const streamChunk = function* (chunk, chunkSize) {
    let len = chunk.byteLength;
    if (len < chunkSize) {
      yield chunk;
      return;
    }
    let pos = 0;
    let end;
    while (pos < len) {
      end = pos + chunkSize;
      yield chunk.slice(pos, end);
      pos = end;
    }
  };
  const readBytes = async function* (iterable, chunkSize) {
    for await (const chunk of readStream(iterable)) {
      yield* streamChunk(chunk, chunkSize);
    }
  };
  const readStream = async function* (stream) {
    if (stream[Symbol.asyncIterator]) {
      yield* stream;
      return;
    }
    const reader = stream.getReader();
    try {
      for (; ; ) {
        const { done, value } = await reader.read();
        if (done) {
          break;
        }
        yield value;
      }
    } finally {
      await reader.cancel();
    }
  };
  const trackStream = (stream, chunkSize, onProgress, onFinish) => {
    const iterator2 = readBytes(stream, chunkSize);
    let bytes = 0;
    let done;
    let _onFinish = (e) => {
      if (!done) {
        done = true;
        onFinish && onFinish(e);
      }
    };
    return new ReadableStream(
      {
        async pull(controller) {
          try {
            const { done: done2, value } = await iterator2.next();
            if (done2) {
              _onFinish();
              controller.close();
              return;
            }
            let len = value.byteLength;
            if (onProgress) {
              let loadedBytes = bytes += len;
              onProgress(loadedBytes);
            }
            controller.enqueue(new Uint8Array(value));
          } catch (err) {
            _onFinish(err);
            throw err;
          }
        },
        cancel(reason) {
          _onFinish(reason);
          return iterator2.return();
        }
      },
      {
        highWaterMark: 2
      }
    );
  };
  const DEFAULT_CHUNK_SIZE = 64 * 1024;
  const { isFunction } = utils$1;
  const globalFetchAPI = (({ Request, Response }) => ({
    Request,
    Response
  }))(utils$1.global);
  const { ReadableStream: ReadableStream$1, TextEncoder } = utils$1.global;
  const test = (fn, ...args) => {
    try {
      return !!fn(...args);
    } catch (e) {
      return false;
    }
  };
  const factory = (env) => {
    env = utils$1.merge.call(
      {
        skipUndefined: true
      },
      globalFetchAPI,
      env
    );
    const { fetch: envFetch, Request, Response } = env;
    const isFetchSupported = envFetch ? isFunction(envFetch) : typeof fetch === "function";
    const isRequestSupported = isFunction(Request);
    const isResponseSupported = isFunction(Response);
    if (!isFetchSupported) {
      return false;
    }
    const isReadableStreamSupported = isFetchSupported && isFunction(ReadableStream$1);
    const encodeText = isFetchSupported && (typeof TextEncoder === "function" ? /* @__PURE__ */ ((encoder) => (str) => encoder.encode(str))(new TextEncoder()) : async (str) => new Uint8Array(await new Request(str).arrayBuffer()));
    const supportsRequestStream = isRequestSupported && isReadableStreamSupported && test(() => {
      let duplexAccessed = false;
      const hasContentType = new Request(platform.origin, {
        body: new ReadableStream$1(),
        method: "POST",
        get duplex() {
          duplexAccessed = true;
          return "half";
        }
      }).headers.has("Content-Type");
      return duplexAccessed && !hasContentType;
    });
    const supportsResponseStream = isResponseSupported && isReadableStreamSupported && test(() => utils$1.isReadableStream(new Response("").body));
    const resolvers = {
      stream: supportsResponseStream && ((res) => res.body)
    };
    isFetchSupported && (() => {
      ["text", "arrayBuffer", "blob", "formData", "stream"].forEach((type) => {
        !resolvers[type] && (resolvers[type] = (res, config) => {
          let method = res && res[type];
          if (method) {
            return method.call(res);
          }
          throw new AxiosError$1(
            `Response type '${type}' is not supported`,
            AxiosError$1.ERR_NOT_SUPPORT,
            config
          );
        });
      });
    })();
    const getBodyLength = async (body) => {
      if (body == null) {
        return 0;
      }
      if (utils$1.isBlob(body)) {
        return body.size;
      }
      if (utils$1.isSpecCompliantForm(body)) {
        const _request = new Request(platform.origin, {
          method: "POST",
          body
        });
        return (await _request.arrayBuffer()).byteLength;
      }
      if (utils$1.isArrayBufferView(body) || utils$1.isArrayBuffer(body)) {
        return body.byteLength;
      }
      if (utils$1.isURLSearchParams(body)) {
        body = body + "";
      }
      if (utils$1.isString(body)) {
        return (await encodeText(body)).byteLength;
      }
    };
    const resolveBodyLength = async (headers, body) => {
      const length = utils$1.toFiniteNumber(headers.getContentLength());
      return length == null ? getBodyLength(body) : length;
    };
    return async (config) => {
      let {
        url,
        method,
        data,
        signal,
        cancelToken,
        timeout,
        onDownloadProgress,
        onUploadProgress,
        responseType,
        headers,
        withCredentials = "same-origin",
        fetchOptions
      } = resolveConfig(config);
      let _fetch = envFetch || fetch;
      responseType = responseType ? (responseType + "").toLowerCase() : "text";
      let composedSignal = composeSignals(
        [signal, cancelToken && cancelToken.toAbortSignal()],
        timeout
      );
      let request = null;
      const unsubscribe = composedSignal && composedSignal.unsubscribe && (() => {
        composedSignal.unsubscribe();
      });
      let requestContentLength;
      try {
        if (onUploadProgress && supportsRequestStream && method !== "get" && method !== "head" && (requestContentLength = await resolveBodyLength(headers, data)) !== 0) {
          let _request = new Request(url, {
            method: "POST",
            body: data,
            duplex: "half"
          });
          let contentTypeHeader;
          if (utils$1.isFormData(data) && (contentTypeHeader = _request.headers.get("content-type"))) {
            headers.setContentType(contentTypeHeader);
          }
          if (_request.body) {
            const [onProgress, flush] = progressEventDecorator(
              requestContentLength,
              progressEventReducer(asyncDecorator(onUploadProgress))
            );
            data = trackStream(_request.body, DEFAULT_CHUNK_SIZE, onProgress, flush);
          }
        }
        if (!utils$1.isString(withCredentials)) {
          withCredentials = withCredentials ? "include" : "omit";
        }
        const isCredentialsSupported = isRequestSupported && "credentials" in Request.prototype;
        const resolvedOptions = {
          ...fetchOptions,
          signal: composedSignal,
          method: method.toUpperCase(),
          headers: headers.normalize().toJSON(),
          body: data,
          duplex: "half",
          credentials: isCredentialsSupported ? withCredentials : void 0
        };
        request = isRequestSupported && new Request(url, resolvedOptions);
        let response = await (isRequestSupported ? _fetch(request, fetchOptions) : _fetch(url, resolvedOptions));
        const isStreamResponse = supportsResponseStream && (responseType === "stream" || responseType === "response");
        if (supportsResponseStream && (onDownloadProgress || isStreamResponse && unsubscribe)) {
          const options = {};
          ["status", "statusText", "headers"].forEach((prop) => {
            options[prop] = response[prop];
          });
          const responseContentLength = utils$1.toFiniteNumber(response.headers.get("content-length"));
          const [onProgress, flush] = onDownloadProgress && progressEventDecorator(
            responseContentLength,
            progressEventReducer(asyncDecorator(onDownloadProgress), true)
          ) || [];
          response = new Response(
            trackStream(response.body, DEFAULT_CHUNK_SIZE, onProgress, () => {
              flush && flush();
              unsubscribe && unsubscribe();
            }),
            options
          );
        }
        responseType = responseType || "text";
        let responseData = await resolvers[utils$1.findKey(resolvers, responseType) || "text"](
          response,
          config
        );
        !isStreamResponse && unsubscribe && unsubscribe();
        return await new Promise((resolve, reject) => {
          settle(resolve, reject, {
            data: responseData,
            headers: AxiosHeaders$1.from(response.headers),
            status: response.status,
            statusText: response.statusText,
            config,
            request
          });
        });
      } catch (err) {
        unsubscribe && unsubscribe();
        if (err && err.name === "TypeError" && /Load failed|fetch/i.test(err.message)) {
          throw Object.assign(
            new AxiosError$1(
              "Network Error",
              AxiosError$1.ERR_NETWORK,
              config,
              request,
              err && err.response
            ),
            {
              cause: err.cause || err
            }
          );
        }
        throw AxiosError$1.from(err, err && err.code, config, request, err && err.response);
      }
    };
  };
  const seedCache = /* @__PURE__ */ new Map();
  const getFetch = (config) => {
    let env = config && config.env || {};
    const { fetch: fetch2, Request, Response } = env;
    const seeds = [Request, Response, fetch2];
    let len = seeds.length, i = len, seed, target, map = seedCache;
    while (i--) {
      seed = seeds[i];
      target = map.get(seed);
      target === void 0 && map.set(seed, target = i ? /* @__PURE__ */ new Map() : factory(env));
      map = target;
    }
    return target;
  };
  getFetch();
  const knownAdapters = {
    http: httpAdapter,
    xhr: xhrAdapter,
    fetch: {
      get: getFetch
    }
  };
  utils$1.forEach(knownAdapters, (fn, value) => {
    if (fn) {
      try {
        Object.defineProperty(fn, "name", { value });
      } catch (e) {
      }
      Object.defineProperty(fn, "adapterName", { value });
    }
  });
  const renderReason = (reason) => `- ${reason}`;
  const isResolvedHandle = (adapter) => utils$1.isFunction(adapter) || adapter === null || adapter === false;
  function getAdapter$1(adapters2, config) {
    adapters2 = utils$1.isArray(adapters2) ? adapters2 : [adapters2];
    const { length } = adapters2;
    let nameOrAdapter;
    let adapter;
    const rejectedReasons = {};
    for (let i = 0; i < length; i++) {
      nameOrAdapter = adapters2[i];
      let id;
      adapter = nameOrAdapter;
      if (!isResolvedHandle(nameOrAdapter)) {
        adapter = knownAdapters[(id = String(nameOrAdapter)).toLowerCase()];
        if (adapter === void 0) {
          throw new AxiosError$1(`Unknown adapter '${id}'`);
        }
      }
      if (adapter && (utils$1.isFunction(adapter) || (adapter = adapter.get(config)))) {
        break;
      }
      rejectedReasons[id || "#" + i] = adapter;
    }
    if (!adapter) {
      const reasons = Object.entries(rejectedReasons).map(
        ([id, state]) => `adapter ${id} ` + (state === false ? "is not supported by the environment" : "is not available in the build")
      );
      let s = length ? reasons.length > 1 ? "since :\n" + reasons.map(renderReason).join("\n") : " " + renderReason(reasons[0]) : "as no adapter specified";
      throw new AxiosError$1(
        `There is no suitable adapter to dispatch the request ` + s,
        "ERR_NOT_SUPPORT"
      );
    }
    return adapter;
  }
  const adapters = {
    /**
     * Resolve an adapter from a list of adapter names or functions.
     * @type {Function}
     */
    getAdapter: getAdapter$1,
    /**
     * Exposes all known adapters
     * @type {Object<string, Function|Object>}
     */
    adapters: knownAdapters
  };
  function throwIfCancellationRequested(config) {
    if (config.cancelToken) {
      config.cancelToken.throwIfRequested();
    }
    if (config.signal && config.signal.aborted) {
      throw new CanceledError$1(null, config);
    }
  }
  function dispatchRequest(config) {
    throwIfCancellationRequested(config);
    config.headers = AxiosHeaders$1.from(config.headers);
    config.data = transformData.call(config, config.transformRequest);
    if (["post", "put", "patch"].indexOf(config.method) !== -1) {
      config.headers.setContentType("application/x-www-form-urlencoded", false);
    }
    const adapter = adapters.getAdapter(config.adapter || defaults.adapter, config);
    return adapter(config).then(
      function onAdapterResolution(response) {
        throwIfCancellationRequested(config);
        response.data = transformData.call(config, config.transformResponse, response);
        response.headers = AxiosHeaders$1.from(response.headers);
        return response;
      },
      function onAdapterRejection(reason) {
        if (!isCancel$1(reason)) {
          throwIfCancellationRequested(config);
          if (reason && reason.response) {
            reason.response.data = transformData.call(
              config,
              config.transformResponse,
              reason.response
            );
            reason.response.headers = AxiosHeaders$1.from(reason.response.headers);
          }
        }
        return Promise.reject(reason);
      }
    );
  }
  const VERSION$1 = "1.13.6";
  const validators$1 = {};
  ["object", "boolean", "number", "function", "string", "symbol"].forEach((type, i) => {
    validators$1[type] = function validator2(thing) {
      return typeof thing === type || "a" + (i < 1 ? "n " : " ") + type;
    };
  });
  const deprecatedWarnings = {};
  validators$1.transitional = function transitional(validator2, version, message) {
    function formatMessage(opt, desc) {
      return "[Axios v" + VERSION$1 + "] Transitional option '" + opt + "'" + desc + (message ? ". " + message : "");
    }
    return (value, opt, opts) => {
      if (validator2 === false) {
        throw new AxiosError$1(
          formatMessage(opt, " has been removed" + (version ? " in " + version : "")),
          AxiosError$1.ERR_DEPRECATED
        );
      }
      if (version && !deprecatedWarnings[opt]) {
        deprecatedWarnings[opt] = true;
        console.warn(
          formatMessage(
            opt,
            " has been deprecated since v" + version + " and will be removed in the near future"
          )
        );
      }
      return validator2 ? validator2(value, opt, opts) : true;
    };
  };
  validators$1.spelling = function spelling(correctSpelling) {
    return (value, opt) => {
      console.warn(`${opt} is likely a misspelling of ${correctSpelling}`);
      return true;
    };
  };
  function assertOptions(options, schema, allowUnknown) {
    if (typeof options !== "object") {
      throw new AxiosError$1("options must be an object", AxiosError$1.ERR_BAD_OPTION_VALUE);
    }
    const keys = Object.keys(options);
    let i = keys.length;
    while (i-- > 0) {
      const opt = keys[i];
      const validator2 = schema[opt];
      if (validator2) {
        const value = options[opt];
        const result = value === void 0 || validator2(value, opt, options);
        if (result !== true) {
          throw new AxiosError$1(
            "option " + opt + " must be " + result,
            AxiosError$1.ERR_BAD_OPTION_VALUE
          );
        }
        continue;
      }
      if (allowUnknown !== true) {
        throw new AxiosError$1("Unknown option " + opt, AxiosError$1.ERR_BAD_OPTION);
      }
    }
  }
  const validator = {
    assertOptions,
    validators: validators$1
  };
  const validators = validator.validators;
  let Axios$1 = class Axios {
    constructor(instanceConfig) {
      this.defaults = instanceConfig || {};
      this.interceptors = {
        request: new InterceptorManager(),
        response: new InterceptorManager()
      };
    }
    /**
     * Dispatch a request
     *
     * @param {String|Object} configOrUrl The config specific for this request (merged with this.defaults)
     * @param {?Object} config
     *
     * @returns {Promise} The Promise to be fulfilled
     */
    async request(configOrUrl, config) {
      try {
        return await this._request(configOrUrl, config);
      } catch (err) {
        if (err instanceof Error) {
          let dummy = {};
          Error.captureStackTrace ? Error.captureStackTrace(dummy) : dummy = new Error();
          const stack = dummy.stack ? dummy.stack.replace(/^.+\n/, "") : "";
          try {
            if (!err.stack) {
              err.stack = stack;
            } else if (stack && !String(err.stack).endsWith(stack.replace(/^.+\n.+\n/, ""))) {
              err.stack += "\n" + stack;
            }
          } catch (e) {
          }
        }
        throw err;
      }
    }
    _request(configOrUrl, config) {
      if (typeof configOrUrl === "string") {
        config = config || {};
        config.url = configOrUrl;
      } else {
        config = configOrUrl || {};
      }
      config = mergeConfig$1(this.defaults, config);
      const { transitional, paramsSerializer, headers } = config;
      if (transitional !== void 0) {
        validator.assertOptions(
          transitional,
          {
            silentJSONParsing: validators.transitional(validators.boolean),
            forcedJSONParsing: validators.transitional(validators.boolean),
            clarifyTimeoutError: validators.transitional(validators.boolean),
            legacyInterceptorReqResOrdering: validators.transitional(validators.boolean)
          },
          false
        );
      }
      if (paramsSerializer != null) {
        if (utils$1.isFunction(paramsSerializer)) {
          config.paramsSerializer = {
            serialize: paramsSerializer
          };
        } else {
          validator.assertOptions(
            paramsSerializer,
            {
              encode: validators.function,
              serialize: validators.function
            },
            true
          );
        }
      }
      if (config.allowAbsoluteUrls !== void 0) ;
      else if (this.defaults.allowAbsoluteUrls !== void 0) {
        config.allowAbsoluteUrls = this.defaults.allowAbsoluteUrls;
      } else {
        config.allowAbsoluteUrls = true;
      }
      validator.assertOptions(
        config,
        {
          baseUrl: validators.spelling("baseURL"),
          withXsrfToken: validators.spelling("withXSRFToken")
        },
        true
      );
      config.method = (config.method || this.defaults.method || "get").toLowerCase();
      let contextHeaders = headers && utils$1.merge(headers.common, headers[config.method]);
      headers && utils$1.forEach(["delete", "get", "head", "post", "put", "patch", "common"], (method) => {
        delete headers[method];
      });
      config.headers = AxiosHeaders$1.concat(contextHeaders, headers);
      const requestInterceptorChain = [];
      let synchronousRequestInterceptors = true;
      this.interceptors.request.forEach(function unshiftRequestInterceptors(interceptor) {
        if (typeof interceptor.runWhen === "function" && interceptor.runWhen(config) === false) {
          return;
        }
        synchronousRequestInterceptors = synchronousRequestInterceptors && interceptor.synchronous;
        const transitional2 = config.transitional || transitionalDefaults;
        const legacyInterceptorReqResOrdering = transitional2 && transitional2.legacyInterceptorReqResOrdering;
        if (legacyInterceptorReqResOrdering) {
          requestInterceptorChain.unshift(interceptor.fulfilled, interceptor.rejected);
        } else {
          requestInterceptorChain.push(interceptor.fulfilled, interceptor.rejected);
        }
      });
      const responseInterceptorChain = [];
      this.interceptors.response.forEach(function pushResponseInterceptors(interceptor) {
        responseInterceptorChain.push(interceptor.fulfilled, interceptor.rejected);
      });
      let promise;
      let i = 0;
      let len;
      if (!synchronousRequestInterceptors) {
        const chain = [dispatchRequest.bind(this), void 0];
        chain.unshift(...requestInterceptorChain);
        chain.push(...responseInterceptorChain);
        len = chain.length;
        promise = Promise.resolve(config);
        while (i < len) {
          promise = promise.then(chain[i++], chain[i++]);
        }
        return promise;
      }
      len = requestInterceptorChain.length;
      let newConfig = config;
      while (i < len) {
        const onFulfilled = requestInterceptorChain[i++];
        const onRejected = requestInterceptorChain[i++];
        try {
          newConfig = onFulfilled(newConfig);
        } catch (error) {
          onRejected.call(this, error);
          break;
        }
      }
      try {
        promise = dispatchRequest.call(this, newConfig);
      } catch (error) {
        return Promise.reject(error);
      }
      i = 0;
      len = responseInterceptorChain.length;
      while (i < len) {
        promise = promise.then(responseInterceptorChain[i++], responseInterceptorChain[i++]);
      }
      return promise;
    }
    getUri(config) {
      config = mergeConfig$1(this.defaults, config);
      const fullPath = buildFullPath(config.baseURL, config.url, config.allowAbsoluteUrls);
      return buildURL(fullPath, config.params, config.paramsSerializer);
    }
  };
  utils$1.forEach(["delete", "get", "head", "options"], function forEachMethodNoData(method) {
    Axios$1.prototype[method] = function(url, config) {
      return this.request(
        mergeConfig$1(config || {}, {
          method,
          url,
          data: (config || {}).data
        })
      );
    };
  });
  utils$1.forEach(["post", "put", "patch"], function forEachMethodWithData(method) {
    function generateHTTPMethod(isForm) {
      return function httpMethod(url, data, config) {
        return this.request(
          mergeConfig$1(config || {}, {
            method,
            headers: isForm ? {
              "Content-Type": "multipart/form-data"
            } : {},
            url,
            data
          })
        );
      };
    }
    Axios$1.prototype[method] = generateHTTPMethod();
    Axios$1.prototype[method + "Form"] = generateHTTPMethod(true);
  });
  let CancelToken$1 = class CancelToken2 {
    constructor(executor) {
      if (typeof executor !== "function") {
        throw new TypeError("executor must be a function.");
      }
      let resolvePromise;
      this.promise = new Promise(function promiseExecutor(resolve) {
        resolvePromise = resolve;
      });
      const token = this;
      this.promise.then((cancel) => {
        if (!token._listeners) return;
        let i = token._listeners.length;
        while (i-- > 0) {
          token._listeners[i](cancel);
        }
        token._listeners = null;
      });
      this.promise.then = (onfulfilled) => {
        let _resolve;
        const promise = new Promise((resolve) => {
          token.subscribe(resolve);
          _resolve = resolve;
        }).then(onfulfilled);
        promise.cancel = function reject() {
          token.unsubscribe(_resolve);
        };
        return promise;
      };
      executor(function cancel(message, config, request) {
        if (token.reason) {
          return;
        }
        token.reason = new CanceledError$1(message, config, request);
        resolvePromise(token.reason);
      });
    }
    /**
     * Throws a `CanceledError` if cancellation has been requested.
     */
    throwIfRequested() {
      if (this.reason) {
        throw this.reason;
      }
    }
    /**
     * Subscribe to the cancel signal
     */
    subscribe(listener) {
      if (this.reason) {
        listener(this.reason);
        return;
      }
      if (this._listeners) {
        this._listeners.push(listener);
      } else {
        this._listeners = [listener];
      }
    }
    /**
     * Unsubscribe from the cancel signal
     */
    unsubscribe(listener) {
      if (!this._listeners) {
        return;
      }
      const index2 = this._listeners.indexOf(listener);
      if (index2 !== -1) {
        this._listeners.splice(index2, 1);
      }
    }
    toAbortSignal() {
      const controller = new AbortController();
      const abort = (err) => {
        controller.abort(err);
      };
      this.subscribe(abort);
      controller.signal.unsubscribe = () => this.unsubscribe(abort);
      return controller.signal;
    }
    /**
     * Returns an object that contains a new `CancelToken` and a function that, when called,
     * cancels the `CancelToken`.
     */
    static source() {
      let cancel;
      const token = new CancelToken2(function executor(c) {
        cancel = c;
      });
      return {
        token,
        cancel
      };
    }
  };
  function spread$1(callback) {
    return function wrap(arr) {
      return callback.apply(null, arr);
    };
  }
  function isAxiosError$1(payload) {
    return utils$1.isObject(payload) && payload.isAxiosError === true;
  }
  const HttpStatusCode$1 = {
    Continue: 100,
    SwitchingProtocols: 101,
    Processing: 102,
    EarlyHints: 103,
    Ok: 200,
    Created: 201,
    Accepted: 202,
    NonAuthoritativeInformation: 203,
    NoContent: 204,
    ResetContent: 205,
    PartialContent: 206,
    MultiStatus: 207,
    AlreadyReported: 208,
    ImUsed: 226,
    MultipleChoices: 300,
    MovedPermanently: 301,
    Found: 302,
    SeeOther: 303,
    NotModified: 304,
    UseProxy: 305,
    Unused: 306,
    TemporaryRedirect: 307,
    PermanentRedirect: 308,
    BadRequest: 400,
    Unauthorized: 401,
    PaymentRequired: 402,
    Forbidden: 403,
    NotFound: 404,
    MethodNotAllowed: 405,
    NotAcceptable: 406,
    ProxyAuthenticationRequired: 407,
    RequestTimeout: 408,
    Conflict: 409,
    Gone: 410,
    LengthRequired: 411,
    PreconditionFailed: 412,
    PayloadTooLarge: 413,
    UriTooLong: 414,
    UnsupportedMediaType: 415,
    RangeNotSatisfiable: 416,
    ExpectationFailed: 417,
    ImATeapot: 418,
    MisdirectedRequest: 421,
    UnprocessableEntity: 422,
    Locked: 423,
    FailedDependency: 424,
    TooEarly: 425,
    UpgradeRequired: 426,
    PreconditionRequired: 428,
    TooManyRequests: 429,
    RequestHeaderFieldsTooLarge: 431,
    UnavailableForLegalReasons: 451,
    InternalServerError: 500,
    NotImplemented: 501,
    BadGateway: 502,
    ServiceUnavailable: 503,
    GatewayTimeout: 504,
    HttpVersionNotSupported: 505,
    VariantAlsoNegotiates: 506,
    InsufficientStorage: 507,
    LoopDetected: 508,
    NotExtended: 510,
    NetworkAuthenticationRequired: 511,
    WebServerIsDown: 521,
    ConnectionTimedOut: 522,
    OriginIsUnreachable: 523,
    TimeoutOccurred: 524,
    SslHandshakeFailed: 525,
    InvalidSslCertificate: 526
  };
  Object.entries(HttpStatusCode$1).forEach(([key, value]) => {
    HttpStatusCode$1[value] = key;
  });
  function createInstance(defaultConfig2) {
    const context = new Axios$1(defaultConfig2);
    const instance = bind(Axios$1.prototype.request, context);
    utils$1.extend(instance, Axios$1.prototype, context, { allOwnKeys: true });
    utils$1.extend(instance, context, null, { allOwnKeys: true });
    instance.create = function create(instanceConfig) {
      return createInstance(mergeConfig$1(defaultConfig2, instanceConfig));
    };
    return instance;
  }
  const axios = createInstance(defaults);
  axios.Axios = Axios$1;
  axios.CanceledError = CanceledError$1;
  axios.CancelToken = CancelToken$1;
  axios.isCancel = isCancel$1;
  axios.VERSION = VERSION$1;
  axios.toFormData = toFormData$1;
  axios.AxiosError = AxiosError$1;
  axios.Cancel = axios.CanceledError;
  axios.all = function all2(promises) {
    return Promise.all(promises);
  };
  axios.spread = spread$1;
  axios.isAxiosError = isAxiosError$1;
  axios.mergeConfig = mergeConfig$1;
  axios.AxiosHeaders = AxiosHeaders$1;
  axios.formToJSON = (thing) => formDataToJSON(utils$1.isHTMLForm(thing) ? new FormData(thing) : thing);
  axios.getAdapter = adapters.getAdapter;
  axios.HttpStatusCode = HttpStatusCode$1;
  axios.default = axios;
  const {
    Axios,
    AxiosError,
    CanceledError,
    isCancel,
    CancelToken,
    VERSION,
    all,
    Cancel,
    isAxiosError,
    spread,
    toFormData,
    AxiosHeaders,
    HttpStatusCode,
    formToJSON,
    getAdapter,
    mergeConfig
  } = axios;
  const defaultHeaderConfig = (_config) => {
    const headers = {};
    const token = localStorage.getItem("token");
    if (token) {
      headers.Authorization = `Bearer ${token}`;
    }
    const namespace = localStorage.getItem("namespace") || "default";
    {
      headers["X-Namespace"] = namespace;
    }
    const clientId = localStorage.getItem("gress_sse_client_id");
    if (clientId) {
      headers["X-Client-Id"] = clientId;
    }
    return headers;
  };
  const defaultMessageHandler = {
    success: (msg) => console.log("[Success]", msg),
    error: (msg) => console.error("[Error]", msg),
    warning: (msg) => console.warn("[Warning]", msg),
    info: (msg) => console.info("[Info]", msg)
  };
  const defaultRouterHandler = {
    push: (path) => {
      if (typeof window !== "undefined") {
        window.location.href = path;
      }
    }
  };
  class RequestClient {
    constructor(options = {}) {
      const {
        headerConfig = defaultHeaderConfig,
        messageHandler = defaultMessageHandler,
        routerHandler = defaultRouterHandler,
        ...axiosConfig
      } = options;
      this.headerConfig = headerConfig;
      this.messageHandler = messageHandler;
      this.routerHandler = routerHandler;
      this.instance = axios.create({
        timeout: 3e4,
        ...axiosConfig
      });
      this.setupInterceptors();
    }
    /**
     * 设置 Header 配置函数
     */
    setHeaderConfig(headerConfig) {
      this.headerConfig = headerConfig;
    }
    /**
     * 设置消息处理器
     */
    setMessageHandler(messageHandler) {
      this.messageHandler = messageHandler;
    }
    /**
     * 设置路由处理器
     */
    setRouterHandler(routerHandler) {
      this.routerHandler = routerHandler;
    }
    setupInterceptors() {
      this.instance.interceptors.request.use(
        (config) => {
          if (this.headerConfig) {
            const dynamicHeaders = this.headerConfig(config);
            config.headers = config.headers || {};
            Object.assign(config.headers, dynamicHeaders);
          }
          return config;
        },
        (error) => {
          return Promise.reject(error);
        }
      );
      this.instance.interceptors.response.use(
        (response) => {
          return this.handleResponse(response);
        },
        (error) => {
          return this.handleError(error);
        }
      );
    }
    handleResponse(response) {
      const config = response.config;
      const { isReturnNativeResponse, isTransformResponse = true } = config;
      if (isReturnNativeResponse) {
        return response;
      }
      if (!isTransformResponse) {
        return response.data;
      }
      const responseData = response.data;
      if (!responseData) {
        console.error("响应数据为空:", response);
        throw new Error("请求失败，未返回数据");
      }
      if (typeof responseData !== "object") {
        console.error("响应数据类型错误:", typeof responseData, responseData);
        throw new Error("响应数据格式错误");
      }
      const { code, data, errorMessage: msg } = responseData;
      if (code === void 0 || code === null) {
        console.error("响应数据缺少 code 字段:", responseData);
        throw new Error("响应数据格式错误：缺少 code 字段");
      }
      const hasSuccess = code === 200;
      if (hasSuccess) {
        if (config.successMessageMode === "message" && msg) {
          this.messageHandler.success(msg);
        }
        return data;
      }
      return this.handleBusinessError(code, msg, config);
    }
    handleBusinessError(code, msg, config) {
      const errorMessageMode = config.errorMessageMode ?? "message";
      switch (code) {
        case 401: {
          localStorage.removeItem("token");
          this.messageHandler.error("登录已过期，请重新登录");
          this.routerHandler.push("/login");
          break;
        }
        case 403: {
          this.messageHandler.error("没有权限访问该资源");
          break;
        }
        case 404: {
          this.messageHandler.error("请求的资源不存在");
          break;
        }
        default: {
          if (errorMessageMode === "message" && msg) {
            this.messageHandler.error(msg);
          }
        }
      }
      throw new Error(msg || "请求失败");
    }
    handleError(error) {
      const config = error.config;
      const errorMessageMode = (config == null ? void 0 : config.errorMessageMode) ?? "message";
      let errorMessage = "请求失败";
      if (error.response) {
        const { status, data } = error.response;
        errorMessage = (data == null ? void 0 : data.message) || `请求失败 (${status})`;
      } else if (error.request) {
        errorMessage = "网络错误，请检查网络连接";
      } else {
        errorMessage = error.message || "请求配置错误";
      }
      if (errorMessageMode === "message") {
        this.messageHandler.error(errorMessage);
      }
      return Promise.reject(error);
    }
    // GET 请求
    get(url, config) {
      return this.instance.get(url, config);
    }
    // POST 请求
    post(url, data, config) {
      return this.instance.post(url, data, config);
    }
    // PUT 请求
    put(url, data, config) {
      return this.instance.put(url, data, config);
    }
    // DELETE 请求
    delete(url, config) {
      return this.instance.delete(url, config);
    }
    // PATCH 请求
    patch(url, data, config) {
      return this.instance.patch(url, data, config);
    }
    // 获取原始 axios 实例
    getAxiosInstance() {
      return this.instance;
    }
  }
  function createRequestClient(options = {}) {
    return new RequestClient(options);
  }
  createRequestClient({
    baseURL: "/api",
    headers: {
      "Content-Type": "application/json"
    },
    headerConfig: defaultHeaderConfig
  });
  function getGressBridge() {
    if (typeof window !== "undefined" && window.GressBridge) {
      return window.GressBridge;
    }
    throw new Error("GressBridge未初始化");
  }
  const http = {
    /**
     * GET请求
     */
    async get(url, params) {
      const bridge = getGressBridge();
      let fullUrl = url;
      if (params) {
        const filteredParams = {};
        for (const [key, value] of Object.entries(params)) {
          if (value !== void 0 && value !== null && value !== "") {
            filteredParams[key] = String(value);
          }
        }
        if (Object.keys(filteredParams).length > 0) {
          const queryString = new URLSearchParams(filteredParams).toString();
          fullUrl = `${url}?${queryString}`;
        }
      }
      return bridge.http.get(fullUrl);
    },
    /**
     * POST请求
     * @param url 请求URL
     * @param data 请求数据
     * @param config Axios配置（可选），支持 headers、onUploadProgress 等
     */
    async post(url, data, config) {
      const bridge = getGressBridge();
      return bridge.http.post(url, data, config);
    },
    /**
     * PUT请求
     */
    async put(url, data, config) {
      const bridge = getGressBridge();
      return bridge.http.put(url, data, config);
    },
    /**
     * DELETE请求
     */
    async delete(url, config) {
      const bridge = getGressBridge();
      return bridge.http.delete(url, config);
    },
    /**
     * PATCH请求
     */
    async patch(url, data, config) {
      const bridge = getGressBridge();
      return bridge.http.patch(url, data, config);
    },
    /**
     * 通用请求方法
     */
    async request(config) {
      const bridge = getGressBridge();
      return bridge.http.request(config);
    }
  };
  const API_BASE$2 = "/plugins/appstore-admin";
  const tablePermissionApi = {
    /**
     * 获取所有权限配置
     */
    getList(params) {
      return http.get(`${API_BASE$2}/table-permissions`, params);
    },
    /**
     * 根据ID查询权限配置
     */
    getById(id) {
      return http.get(`${API_BASE$2}/table-permissions/${id}`);
    },
    /**
     * 创建权限配置
     */
    create(data) {
      return http.post(`${API_BASE$2}/table-permissions`, data);
    },
    /**
     * 更新权限配置
     */
    update(id, data) {
      return http.put(`${API_BASE$2}/table-permissions/${id}`, data);
    },
    /**
     * 删除权限配置
     */
    delete(id) {
      return http.delete(`${API_BASE$2}/table-permissions/${id}`);
    },
    /**
     * 启用/禁用权限配置
     */
    setEnabled(id, enabled) {
      return http.put(`${API_BASE$2}/table-permissions/${id}/enabled?enabled=${enabled}`);
    }
  };
  const API_BASE$1 = "/plugins/appstore-admin";
  const permissionRequestApi = {
    /**
     * 获取待审核申请列表
     */
    getPendingList() {
      return http.get(`${API_BASE$1}/permission-requests/pending`);
    },
    /**
     * 获取所有申请（支持筛选）
     */
    getList(params) {
      return http.get(`${API_BASE$1}/permission-requests`, params);
    },
    /**
     * 根据ID查询申请
     */
    getById(id) {
      return http.get(`${API_BASE$1}/permission-requests/${id}`);
    },
    /**
     * 批准申请
     */
    approve(id, data) {
      return http.post(`${API_BASE$1}/permission-requests/${id}/approve`, data);
    },
    /**
     * 拒绝申请
     */
    reject(id, data) {
      return http.post(`${API_BASE$1}/permission-requests/${id}/reject`, data);
    }
  };
  const API_BASE = "/plugins/appstore-admin";
  const submissionApi = {
    /**
     * 获取插件提交列表
     */
    getList(params) {
      return http.get(`${API_BASE}/plugins/submissions`, params);
    },
    /**
     * 获取插件提交详情
     */
    getDetail(id) {
      return http.get(`${API_BASE}/plugins/submissions/${id}`);
    },
    /**
     * 批准插件
     */
    approve(id, data) {
      return http.post(`${API_BASE}/plugins/submissions/${id}/approve`, data);
    },
    /**
     * 拒绝插件
     */
    reject(id, data) {
      return http.post(`${API_BASE}/plugins/submissions/${id}/reject`, data);
    }
  };
  const versionApi = {
    /**
     * 获取插件版本列表
     */
    getList(pluginId) {
      return http.get(`${API_BASE}/plugins/${pluginId}/versions`);
    },
    /**
     * 获取版本详情
     */
    getDetail(pluginId, version) {
      return http.get(`${API_BASE}/plugins/${pluginId}/versions/${version}`);
    },
    /**
     * 设置当前版本
     */
    setCurrent(pluginId, version) {
      return http.post(`${API_BASE}/plugins/${pluginId}/versions/${version}/set-current`);
    },
    /**
     * 回滚到指定版本
     */
    rollback(pluginId, version) {
      return http.post(`${API_BASE}/plugins/${pluginId}/versions/${version}/rollback`);
    },
    /**
     * 删除版本
     */
    delete(pluginId, version) {
      return http.delete(`${API_BASE}/plugins/${pluginId}/versions/${version}`);
    }
  };
  const categoryApi = {
    /**
     * 获取所有分类
     */
    getAll() {
      return http.get(`${API_BASE}/categories`);
    },
    /**
     * 获取分类详情
     */
    getById(id) {
      return http.get(`${API_BASE}/categories/${id}`);
    },
    /**
     * 创建分类
     */
    create(data) {
      return http.post(`${API_BASE}/categories`, data);
    },
    /**
     * 更新分类
     */
    update(id, data) {
      return http.put(`${API_BASE}/categories/${id}`, data);
    },
    /**
     * 删除分类
     */
    delete(id) {
      return http.delete(`${API_BASE}/categories/${id}`);
    }
  };
  const pluginApi = {
    /**
     * 获取已上架插件列表
     */
    getList(params) {
      return http.get(`${API_BASE}/plugins`, params);
    },
    /**
     * 获取插件类型列表
     */
    getTypes() {
      return http.get(`${API_BASE}/plugins/types`);
    },
    /**
     * 获取插件详情
     */
    getDetail(pluginId) {
      return http.get(`${API_BASE}/plugins/${pluginId}`);
    },
    /**
     * 上传插件包
     */
    upload(formData, onProgress) {
      return http.post(`${API_BASE}/plugins/upload`, formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        },
        onUploadProgress: (progressEvent) => {
          if (onProgress && progressEvent.total) {
            const progress = Math.round(progressEvent.loaded * 100 / progressEvent.total);
            onProgress(progress);
          }
        }
      });
    },
    /**
     * 升级插件版本
     */
    upgrade(formData, onProgress) {
      return http.post(`${API_BASE}/plugins/upgrade`, formData, {
        headers: {
          "Content-Type": "multipart/form-data"
        },
        onUploadProgress: (progressEvent) => {
          if (onProgress && progressEvent.total) {
            const progress = Math.round(progressEvent.loaded * 100 / progressEvent.total);
            onProgress(progress);
          }
        }
      });
    },
    /**
     * 下架插件
     */
    delist(pluginId, data) {
      return http.post(`${API_BASE}/plugins/${pluginId}/delist`, {
        reason: data.reason,
        operatorId: data.operatorId || "admin",
        operatorName: data.operatorName || "管理员"
      });
    },
    /**
     * 重新上架插件
     */
    relist(pluginId, data) {
      return http.post(`${API_BASE}/plugins/${pluginId}/relist`, data || {
        operatorId: "admin",
        operatorName: "管理员"
      });
    },
    /**
     * 更新插件信息
     */
    update(pluginId, data) {
      return http.put(`${API_BASE}/plugins/${pluginId}`, data);
    },
    /**
     * 删除插件
     */
    delete(pluginId) {
      return http.delete(`${API_BASE}/plugins/${pluginId}`);
    }
  };
  const tagApi = {
    /**
     * 获取所有标签
     */
    getAll() {
      return http.get(`${API_BASE}/tags`);
    },
    /**
     * 获取标签详情
     */
    getById(id) {
      return http.get(`${API_BASE}/tags/${id}`);
    },
    /**
     * 创建标签
     */
    create(data) {
      return http.post(`${API_BASE}/tags`, data);
    },
    /**
     * 更新标签
     */
    update(id, data) {
      return http.put(`${API_BASE}/tags/${id}`, data);
    },
    /**
     * 删除标签
     */
    delete(id) {
      return http.delete(`${API_BASE}/tags/${id}`);
    },
    /**
     * 获取插件的标签
     */
    getPluginTags(pluginId) {
      return http.get(`${API_BASE}/tags/plugins/${pluginId}`);
    },
    /**
     * 为插件分配标签
     */
    assignToPlugin(pluginId, tagId) {
      return http.post(`${API_BASE}/tags/plugins/${pluginId}/tags/${tagId}`);
    },
    /**
     * 从插件移除标签
     */
    removeFromPlugin(pluginId, tagId) {
      return http.delete(`${API_BASE}/tags/plugins/${pluginId}/tags/${tagId}`);
    }
  };
  const statisticsApi = {
    /**
     * 获取插件统计数据
     */
    getPluginStatistics(pluginId, params) {
      return http.get(`${API_BASE}/plugins/statistics/${pluginId}`, params);
    },
    /**
     * 获取统计概览
     */
    getOverview() {
      return http.get(`${API_BASE}/plugins/statistics/overview`);
    },
    /**
     * 获取趋势数据
     */
    getTrendData(data) {
      return http.post(`${API_BASE}/plugins/statistics/trending`, data);
    },
    /**
     * 导出统计数据
     */
    exportStatistics(data) {
      return http.post(`${API_BASE}/plugins/statistics/export`, data);
    }
  };
  const developerApi = {
    /**
     * 获取开发者列表
     */
    getList(params) {
      return http.get(`${API_BASE}/developers`, params);
    },
    /**
     * 获取开发者详情
     */
    getDetail(id) {
      return http.get(`${API_BASE}/developers/${id}`);
    },
    /**
     * 批准开发者资格
     */
    approve(id, data) {
      return http.post(`${API_BASE}/developers/${id}/approve`, data);
    },
    /**
     * 暂停开发者账户
     */
    suspend(id, data) {
      return http.post(`${API_BASE}/developers/${id}/suspend`, data);
    },
    /**
     * 激活开发者账户
     */
    activate(id, data) {
      return http.post(`${API_BASE}/developers/${id}/activate`, data);
    }
  };
  const auditLogApi = {
    /**
     * 获取审计日志列表
     */
    getList(params) {
      return http.get(`${API_BASE}/audit-logs`, params);
    },
    /**
     * 获取审计日志详情
     */
    getDetail(id) {
      return http.get(`${API_BASE}/audit-logs/${id}`);
    },
    /**
     * 导出审计日志
     */
    export(data) {
      return http.post(`${API_BASE}/audit-logs/export`, data);
    }
  };
  const reviewRuleApi = {
    /**
     * 获取审核规则列表
     */
    getList(params) {
      return http.get(`${API_BASE}/review-rules`, params);
    },
    /**
     * 获取审核规则详情
     */
    getDetail(id) {
      return http.get(`${API_BASE}/review-rules/${id}`);
    },
    /**
     * 创建审核规则
     */
    create(data) {
      return http.post(`${API_BASE}/review-rules`, data);
    },
    /**
     * 更新审核规则
     */
    update(id, data) {
      return http.put(`${API_BASE}/review-rules/${id}`, data);
    },
    /**
     * 删除审核规则
     */
    delete(id) {
      return http.delete(`${API_BASE}/review-rules/${id}`);
    },
    /**
     * 启用规则
     */
    enable(id) {
      return http.post(`${API_BASE}/review-rules/${id}/enable`);
    },
    /**
     * 禁用规则
     */
    disable(id) {
      return http.post(`${API_BASE}/review-rules/${id}/disable`);
    }
  };
  const feedbackApi = {
    /**
     * 获取反馈列表
     */
    getList(params) {
      return http.get(`${API_BASE}/feedbacks`, params);
    },
    /**
     * 获取反馈详情
     */
    getDetail(id) {
      return http.get(`${API_BASE}/feedbacks/${id}`);
    },
    /**
     * 处理反馈
     */
    process(id, data) {
      return http.post(`${API_BASE}/feedbacks/${id}/process`, data);
    },
    /**
     * 关闭反馈
     */
    close(id, data) {
      return http.post(`${API_BASE}/feedbacks/${id}/close`, data);
    }
  };
  const _hoisted_1$b = { class: "plugin-submissions-page" };
  const _hoisted_2$b = { class: "page-header-wrapper" };
  const _hoisted_3$b = { class: "page-content" };
  const _hoisted_4$b = { class: "table-container" };
  const _hoisted_5$b = { class: "pagination-container" };
  const _sfc_main$b = /* @__PURE__ */ vue.defineComponent({
    __name: "PluginSubmissions",
    setup(__props) {
      const CheckmarkCircle = useIcon("CheckmarkCircleOutline");
      const CloseCircle = useIcon("CloseCircleOutline");
      const Refresh = useIcon("RefreshOutline");
      const Eye = useIcon("EyeOutline");
      const CheckmarkDone = useIcon("CheckmarkDoneOutline");
      const Close = useIcon("CloseOutline");
      const message = useMessage();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const batchApproveLoading = vue.ref(false);
      const batchRejectLoading = vue.ref(false);
      const approveLoading = vue.ref(false);
      const rejectLoading = vue.ref(false);
      const tableData = vue.ref([]);
      const checkedRowKeys = vue.ref([]);
      const showDetailDrawer = vue.ref(false);
      const currentSubmission = vue.ref(null);
      const showAdvanced = vue.ref(false);
      const filters = vue.ref({
        status: "PENDING",
        pluginType: null,
        keyword: "",
        startTime: null,
        endTime: null
      });
      const pagination = vue.reactive({
        page: 1,
        pageSize: 20,
        itemCount: 0,
        pageSizes: [10, 20, 50, 100]
      });
      const handlePageChange = (page) => {
        pagination.page = page;
        loadData();
      };
      const handlePageSizeChange = (pageSize) => {
        pagination.pageSize = pageSize;
        pagination.page = 1;
        loadData();
      };
      const showApproveModal = vue.ref(false);
      const approveForm = vue.reactive({
        comment: ""
      });
      const approveRules = {};
      const approveTargetIds = vue.ref([]);
      const showRejectModal = vue.ref(false);
      const rejectFormRef = vue.ref(null);
      const rejectForm = vue.reactive({
        reason: ""
      });
      const rejectRules = {
        reason: [
          { required: true, message: "请输入拒绝原因", trigger: "blur" }
        ]
      };
      const rejectTargetIds = vue.ref([]);
      const hasSelection = vue.computed(() => checkedRowKeys.value.length > 0);
      const basicFields = [
        {
          key: "status",
          label: "审核状态",
          type: "select",
          placeholder: "请选择状态",
          options: [
            { label: "全部", value: null },
            { label: "待审核", value: "PENDING" },
            { label: "已批准", value: "APPROVED" },
            { label: "已拒绝", value: "REJECTED" }
          ]
        },
        {
          key: "pluginType",
          label: "插件类型",
          type: "select",
          placeholder: "请选择类型",
          options: [
            { label: "全部", value: null },
            { label: "任务节点", value: "TASK" },
            { label: "触发器", value: "TRIGGER" },
            { label: "应用插件", value: "APPLICATION" }
          ]
        },
        {
          key: "keyword",
          label: "关键词",
          type: "input",
          placeholder: "搜索插件名称、开发者"
        }
      ];
      const advancedFields = [
        {
          key: "timeRange",
          label: "提交时间",
          type: "date-range",
          placeholder: "选择时间范围",
          span: 2
        }
      ];
      const columns = [
        {
          type: "selection"
        },
        {
          title: "插件名称",
          key: "pluginName",
          width: 200,
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "插件ID",
          key: "pluginId",
          width: 150,
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "版本",
          key: "version",
          width: 100
        },
        {
          title: "类型",
          key: "pluginType",
          width: 100,
          render: (row) => {
            const NTag = vue.resolveComponent("NTag");
            const typeMap = {
              TASK: { label: "任务节点", type: "info" },
              TRIGGER: { label: "触发器", type: "success" },
              APPLICATION: { label: "应用插件", type: "warning" }
            };
            const config = typeMap[row.pluginType];
            return vue.h(NTag, { type: config.type, size: "small" }, { default: () => config.label });
          }
        },
        {
          title: "开发者",
          key: "developerName",
          width: 120,
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "状态",
          key: "status",
          width: 100,
          render: (row) => {
            const NTag = vue.resolveComponent("NTag");
            const statusMap = {
              PENDING: { label: "待审核", type: "default" },
              APPROVED: { label: "已批准", type: "success" },
              REJECTED: { label: "已拒绝", type: "error" }
            };
            const config = statusMap[row.status];
            return vue.h(NTag, { type: config.type, size: "small" }, { default: () => config.label });
          }
        },
        {
          title: "提交时间",
          key: "submitTime",
          width: 160,
          render: (row) => formatDateTime(row.submitTime)
        },
        {
          title: "操作",
          key: "actions",
          width: 150,
          fixed: "right",
          render: (row) => {
            const NButton = vue.resolveComponent("NButton");
            const NSpace = vue.resolveComponent("NSpace");
            const NIcon = vue.resolveComponent("NIcon");
            const NDropdown = vue.resolveComponent("NDropdown");
            const MoreHorizontal = useIcon("EllipsisHorizontal");
            const moreOptions = [];
            if (row.status === "PENDING") {
              moreOptions.push({
                label: "批准",
                key: "approve",
                icon: () => vue.h(NIcon, { component: CheckmarkDone })
              });
              moreOptions.push({
                label: "拒绝",
                key: "reject",
                icon: () => vue.h(NIcon, { component: Close })
              });
            }
            const handleMoreSelect = (key) => {
              switch (key) {
                case "approve":
                  handleApprove(row.id);
                  break;
                case "reject":
                  handleReject(row.id);
                  break;
              }
            };
            return vue.h(
              NSpace,
              { size: 2, wrap: false },
              {
                default: () => [
                  vue.h(
                    NButton,
                    {
                      size: "small",
                      onClick: () => handleViewDetail(row)
                    },
                    {
                      icon: () => vue.h(NIcon, { component: Eye }),
                      default: () => "查看"
                    }
                  ),
                  moreOptions.length > 0 && vue.h(
                    NDropdown,
                    {
                      trigger: "click",
                      options: moreOptions,
                      onSelect: handleMoreSelect
                    },
                    {
                      default: () => vue.h(
                        NButton,
                        {
                          size: "small",
                          quaternary: true
                        },
                        {
                          icon: () => vue.h(NIcon, { component: MoreHorizontal })
                        }
                      )
                    }
                  )
                ]
              }
            );
          }
        }
      ];
      async function loadData() {
        loading.value = true;
        refreshLoading.value = true;
        try {
          const params = {
            page: pagination.page,
            size: pagination.pageSize,
            status: filters.value.status || void 0,
            type: filters.value.pluginType || void 0,
            keyword: filters.value.keyword || void 0
          };
          const response = await submissionApi.getList(params);
          tableData.value = response.items;
          pagination.itemCount = response.total;
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      }
      function handleViewDetail(submission) {
        currentSubmission.value = submission;
        showDetailDrawer.value = true;
      }
      function handleApprove(id) {
        approveTargetIds.value = Array.isArray(id) ? id : [id];
        approveForm.comment = "";
        showApproveModal.value = true;
      }
      async function confirmApprove() {
        approveLoading.value = true;
        try {
          for (const id of approveTargetIds.value) {
            await submissionApi.approve(id, {
              comment: approveForm.comment,
              reviewerId: "admin",
              // TODO: Get from current user
              reviewerName: "管理员"
              // TODO: Get from current user
            });
          }
          message.success(`成功批准 ${approveTargetIds.value.length} 个插件`);
          showApproveModal.value = false;
          checkedRowKeys.value = [];
          await loadData();
          return true;
        } catch (error) {
          return false;
        } finally {
          approveLoading.value = false;
        }
      }
      function handleReject(id) {
        rejectTargetIds.value = Array.isArray(id) ? id : [id];
        rejectForm.reason = "";
        showRejectModal.value = true;
      }
      async function confirmReject() {
        var _a;
        try {
          await ((_a = rejectFormRef.value) == null ? void 0 : _a.validate());
        } catch {
          return false;
        }
        rejectLoading.value = true;
        try {
          for (const id of rejectTargetIds.value) {
            await submissionApi.reject(id, {
              reason: rejectForm.reason,
              reviewerId: "admin",
              // TODO: Get from current user
              reviewerName: "管理员"
              // TODO: Get from current user
            });
          }
          message.success(`成功拒绝 ${rejectTargetIds.value.length} 个插件`);
          showRejectModal.value = false;
          checkedRowKeys.value = [];
          await loadData();
          return true;
        } catch (error) {
          return false;
        } finally {
          rejectLoading.value = false;
        }
      }
      async function handleBatchApprove() {
        batchApproveLoading.value = true;
        try {
          handleApprove(checkedRowKeys.value);
        } finally {
          batchApproveLoading.value = false;
        }
      }
      async function handleBatchReject() {
        batchRejectLoading.value = true;
        try {
          handleReject(checkedRowKeys.value);
        } finally {
          batchRejectLoading.value = false;
        }
      }
      function handleSearch() {
        pagination.page = 1;
        loadData();
      }
      function handleReset() {
        filters.value.status = "PENDING";
        filters.value.pluginType = null;
        filters.value.keyword = "";
        filters.value.startTime = null;
        filters.value.endTime = null;
        pagination.page = 1;
        loadData();
      }
      function handleCheck(keys) {
        checkedRowKeys.value = keys;
      }
      function formatDateTime(dateStr) {
        if (!dateStr) return "-";
        const date = new Date(dateStr);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit"
        });
      }
      vue.onMounted(() => {
        loadData();
      });
      return (_ctx, _cache) => {
        const _component_n_icon = vue.resolveComponent("n-icon");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_pagination = vue.resolveComponent("n-pagination");
        const _component_n_drawer_content = vue.resolveComponent("n-drawer-content");
        const _component_n_drawer = vue.resolveComponent("n-drawer");
        const _component_n_input = vue.resolveComponent("n-input");
        const _component_n_form_item = vue.resolveComponent("n-form-item");
        const _component_n_form = vue.resolveComponent("n-form");
        const _component_n_modal = vue.resolveComponent("n-modal");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$b, [
          vue.createElementVNode("div", _hoisted_2$b, [
            vue.createVNode(_component_PageHeader, {
              title: "插件审核管理",
              subtitle: "审核开发者提交的插件申请"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(_component_n_button, {
                  type: "primary",
                  onClick: handleBatchApprove,
                  disabled: !hasSelection.value,
                  loading: batchApproveLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(CheckmarkCircle))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[10] || (_cache[10] = vue.createTextVNode(" 批量批准 ", -1))
                  ]),
                  _: 1
                }, 8, ["disabled", "loading"]),
                vue.createVNode(_component_n_button, {
                  onClick: handleBatchReject,
                  disabled: !hasSelection.value,
                  loading: batchRejectLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(CloseCircle))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[11] || (_cache[11] = vue.createTextVNode(" 批量拒绝 ", -1))
                  ]),
                  _: 1
                }, 8, ["disabled", "loading"]),
                vue.createVNode(_component_n_button, {
                  onClick: loadData,
                  loading: refreshLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[12] || (_cache[12] = vue.createTextVNode(" 刷新 ", -1))
                  ]),
                  _: 1
                }, 8, ["loading"])
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$b, [
            vue.createVNode(_component_FilterPanel, {
              filters: filters.value,
              "onUpdate:filters": _cache[0] || (_cache[0] = ($event) => filters.value = $event),
              "show-advanced": showAdvanced.value,
              "onUpdate:showAdvanced": _cache[1] || (_cache[1] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields,
              "advanced-fields": advancedFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced"]),
            vue.createElementVNode("div", _hoisted_4$b, [
              vue.createVNode(_component_n_data_table, {
                columns,
                data: tableData.value,
                loading: loading.value,
                pagination: false,
                "row-key": (row) => row.id,
                "checked-row-keys": checkedRowKeys.value,
                "onUpdate:checkedRowKeys": handleCheck,
                striped: ""
              }, null, 8, ["data", "loading", "row-key", "checked-row-keys"]),
              vue.createElementVNode("div", _hoisted_5$b, [
                vue.createVNode(_component_n_pagination, {
                  page: pagination.page,
                  "onUpdate:page": [
                    _cache[2] || (_cache[2] = ($event) => pagination.page = $event),
                    handlePageChange
                  ],
                  "page-size": pagination.pageSize,
                  "onUpdate:pageSize": [
                    _cache[3] || (_cache[3] = ($event) => pagination.pageSize = $event),
                    handlePageSizeChange
                  ],
                  "item-count": pagination.itemCount,
                  "page-sizes": pagination.pageSizes,
                  "show-size-picker": "",
                  "show-quick-jumper": ""
                }, {
                  prefix: vue.withCtx(({ itemCount }) => [
                    vue.createTextVNode(" 共 " + vue.toDisplayString(itemCount) + " 条 ", 1)
                  ]),
                  _: 1
                }, 8, ["page", "page-size", "item-count", "page-sizes"])
              ])
            ])
          ]),
          vue.createVNode(_component_n_drawer, {
            show: showDetailDrawer.value,
            "onUpdate:show": _cache[5] || (_cache[5] = ($event) => showDetailDrawer.value = $event),
            width: 720,
            placement: "right"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_drawer_content, {
                title: "插件详情",
                closable: ""
              }, {
                default: vue.withCtx(() => [
                  currentSubmission.value ? (vue.openBlock(), vue.createBlock(PluginSubmissionDetail, {
                    key: 0,
                    submission: currentSubmission.value,
                    onApprove: handleApprove,
                    onReject: handleReject,
                    onClose: _cache[4] || (_cache[4] = ($event) => showDetailDrawer.value = false)
                  }, null, 8, ["submission"])) : vue.createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["show"]),
          vue.createVNode(_component_n_modal, {
            show: showApproveModal.value,
            "onUpdate:show": _cache[7] || (_cache[7] = ($event) => showApproveModal.value = $event),
            preset: "dialog",
            title: "批准插件",
            "positive-text": "确认批准",
            "negative-text": "取消",
            "positive-button-props": { loading: approveLoading.value },
            onPositiveClick: confirmApprove
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                model: approveForm,
                rules: approveRules
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "审核意见",
                    path: "comment"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: approveForm.comment,
                        "onUpdate:value": _cache[6] || (_cache[6] = ($event) => approveForm.comment = $event),
                        type: "textarea",
                        placeholder: "请输入审核意见（可选）",
                        rows: 4
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show", "positive-button-props"]),
          vue.createVNode(_component_n_modal, {
            show: showRejectModal.value,
            "onUpdate:show": _cache[9] || (_cache[9] = ($event) => showRejectModal.value = $event),
            preset: "dialog",
            title: "拒绝插件",
            "positive-text": "确认拒绝",
            "negative-text": "取消",
            "positive-button-props": { loading: rejectLoading.value },
            onPositiveClick: confirmReject
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "rejectFormRef",
                ref: rejectFormRef,
                model: rejectForm,
                rules: rejectRules
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "拒绝原因",
                    path: "reason"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: rejectForm.reason,
                        "onUpdate:value": _cache[8] || (_cache[8] = ($event) => rejectForm.reason = $event),
                        type: "textarea",
                        placeholder: "请输入拒绝原因（必填）",
                        rows: 4
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show", "positive-button-props"])
        ]);
      };
    }
  });
  const PluginSubmissions = /* @__PURE__ */ _export_sfc(_sfc_main$b, [["__scopeId", "data-v-50381363"]]);
  const _hoisted_1$a = { class: "plugin-detail" };
  const _hoisted_2$a = { class: "description-content" };
  const _hoisted_3$a = {
    key: 0,
    class: "version-list"
  };
  const _hoisted_4$a = { class: "version-header" };
  const _hoisted_5$a = { class: "version-info" };
  const _hoisted_6$6 = { class: "version-number" };
  const _hoisted_7$5 = { class: "version-time" };
  const _hoisted_8$4 = {
    key: 0,
    class: "version-notes"
  };
  const _hoisted_9$3 = { class: "version-meta" };
  const _hoisted_10$1 = { style: { "margin-left": "16px" } };
  const _hoisted_11$1 = { class: "icon-preview" };
  const _hoisted_12$1 = ["src"];
  const _hoisted_13$1 = {
    key: 1,
    class: "empty-text"
  };
  const _hoisted_14$1 = { class: "rating-text" };
  const _hoisted_15$1 = { class: "action-buttons" };
  const _sfc_main$a = /* @__PURE__ */ vue.defineComponent({
    __name: "PluginDetail",
    props: {
      plugin: {}
    },
    emits: ["delist", "relist", "edit", "close"],
    setup(__props) {
      const CreateOutline = useIcon("CreateOutline");
      const BanOutline = useIcon("BanOutline");
      const CheckmarkCircle = useIcon("CheckmarkCircleOutline");
      const Refresh = useIcon("RefreshOutline");
      useMessage();
      const props = __props;
      const versions = vue.ref([]);
      const loadingVersions = vue.ref(false);
      async function loadVersions() {
        var _a;
        if (!((_a = props.plugin) == null ? void 0 : _a.pluginId)) return;
        loadingVersions.value = true;
        try {
          const response = await versionApi.getList(props.plugin.pluginId);
          versions.value = response;
        } finally {
          loadingVersions.value = false;
        }
      }
      vue.watch(() => {
        var _a;
        return (_a = props.plugin) == null ? void 0 : _a.pluginId;
      }, (newPluginId) => {
        if (newPluginId) {
          loadVersions();
        }
      }, { immediate: true });
      function getPluginTypeTag(type) {
        const typeMap = {
          TASK: { label: "任务节点", type: "info" },
          TRIGGER: { label: "触发器", type: "success" },
          APPLICATION: { label: "应用插件", type: "warning" }
        };
        return typeMap[type];
      }
      function getStatusTag(status) {
        const statusMap = {
          ONLINE: { label: "在线", type: "success" },
          OFFLINE: { label: "离线", type: "warning" },
          DELISTED: { label: "已下架", type: "error" }
        };
        return statusMap[status];
      }
      function getVersionStatusTag(status) {
        const statusMap = {
          ONLINE: { label: "在线", type: "success" },
          OFFLINE: { label: "离线", type: "warning" },
          DELISTED: { label: "已下架", type: "error" }
        };
        return statusMap[status] || { label: status, type: "default" };
      }
      function formatDateTime(dateStr) {
        if (!dateStr) return "-";
        const date = new Date(dateStr);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit"
        });
      }
      function formatFileSize(bytes) {
        if (!bytes || bytes === 0) return "0 B";
        const k = 1024;
        const sizes = ["B", "KB", "MB", "GB"];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return Math.round(bytes / Math.pow(k, i) * 100) / 100 + " " + sizes[i];
      }
      return (_ctx, _cache) => {
        const _component_n_descriptions_item = vue.resolveComponent("n-descriptions-item");
        const _component_n_tag = vue.resolveComponent("n-tag");
        const _component_n_descriptions = vue.resolveComponent("n-descriptions");
        const _component_n_card = vue.resolveComponent("n-card");
        const _component_n_icon = vue.resolveComponent("n-icon");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_n_empty = vue.resolveComponent("n-empty");
        const _component_n_spin = vue.resolveComponent("n-spin");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_n_rate = vue.resolveComponent("n-rate");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$a, [
          vue.createVNode(_component_n_card, {
            title: "基本信息",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_descriptions, {
                column: 2,
                "label-placement": "left"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_descriptions_item, { label: "插件名称" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.plugin.pluginName), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "插件ID" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.plugin.pluginId), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "当前版本" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.plugin.currentVersion), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "插件类型" }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_tag, {
                        type: getPluginTypeTag(__props.plugin.pluginType).type,
                        size: "small"
                      }, {
                        default: vue.withCtx(() => [
                          vue.createTextVNode(vue.toDisplayString(getPluginTypeTag(__props.plugin.pluginType).label), 1)
                        ]),
                        _: 1
                      }, 8, ["type"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "开发者" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.plugin.developerName || __props.plugin.developerId), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "插件状态" }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_tag, {
                        type: getStatusTag(__props.plugin.status).type,
                        size: "small"
                      }, {
                        default: vue.withCtx(() => [
                          vue.createTextVNode(vue.toDisplayString(getStatusTag(__props.plugin.status).label), 1)
                        ]),
                        _: 1
                      }, 8, ["type"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, {
                    label: "上架时间",
                    span: 2
                  }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(formatDateTime(__props.plugin.listingTime)), 1)
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              })
            ]),
            _: 1
          }),
          vue.createVNode(_component_n_card, {
            title: "插件描述",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createElementVNode("div", _hoisted_2$a, vue.toDisplayString(__props.plugin.description || "暂无描述"), 1)
            ]),
            _: 1
          }),
          vue.createVNode(_component_n_card, {
            title: "历史版本",
            bordered: false,
            class: "detail-card"
          }, {
            "header-extra": vue.withCtx(() => [
              vue.createVNode(_component_n_button, {
                text: "",
                onClick: loadVersions,
                loading: loadingVersions.value
              }, {
                icon: vue.withCtx(() => [
                  vue.createVNode(_component_n_icon, null, {
                    default: vue.withCtx(() => [
                      (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                    ]),
                    _: 1
                  })
                ]),
                default: vue.withCtx(() => [
                  _cache[4] || (_cache[4] = vue.createTextVNode(" 刷新 ", -1))
                ]),
                _: 1
              }, 8, ["loading"])
            ]),
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_spin, { show: loadingVersions.value }, {
                default: vue.withCtx(() => [
                  versions.value.length > 0 ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_3$a, [
                    (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(versions.value, (version) => {
                      return vue.openBlock(), vue.createElementBlock("div", {
                        key: version.version,
                        class: "version-item"
                      }, [
                        vue.createElementVNode("div", _hoisted_4$a, [
                          vue.createElementVNode("div", _hoisted_5$a, [
                            vue.createElementVNode("span", _hoisted_6$6, vue.toDisplayString(version.version), 1),
                            version.isCurrent ? (vue.openBlock(), vue.createBlock(_component_n_tag, {
                              key: 0,
                              type: "success",
                              size: "small",
                              style: { "margin-left": "8px" }
                            }, {
                              default: vue.withCtx(() => [..._cache[5] || (_cache[5] = [
                                vue.createTextVNode(" 当前版本 ", -1)
                              ])]),
                              _: 1
                            })) : vue.createCommentVNode("", true),
                            vue.createVNode(_component_n_tag, {
                              type: getVersionStatusTag(version.status).type,
                              size: "small",
                              style: { "margin-left": "8px" }
                            }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(getVersionStatusTag(version.status).label), 1)
                              ]),
                              _: 2
                            }, 1032, ["type"])
                          ]),
                          vue.createElementVNode("span", _hoisted_7$5, vue.toDisplayString(formatDateTime(version.uploadTime)), 1)
                        ]),
                        version.releaseNotes ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_8$4, vue.toDisplayString(version.releaseNotes), 1)) : vue.createCommentVNode("", true),
                        vue.createElementVNode("div", _hoisted_9$3, [
                          vue.createElementVNode("span", null, "文件大小: " + vue.toDisplayString(formatFileSize(version.fileSize)), 1),
                          vue.createElementVNode("span", _hoisted_10$1, "下载次数: " + vue.toDisplayString(version.downloadCount || 0), 1)
                        ])
                      ]);
                    }), 128))
                  ])) : (vue.openBlock(), vue.createBlock(_component_n_empty, {
                    key: 1,
                    description: "暂无版本记录",
                    size: "small"
                  }))
                ]),
                _: 1
              }, 8, ["show"])
            ]),
            _: 1
          }),
          __props.plugin.icon ? (vue.openBlock(), vue.createBlock(_component_n_card, {
            key: 0,
            title: "插件图标",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createElementVNode("div", _hoisted_11$1, [
                vue.createElementVNode("img", {
                  src: __props.plugin.icon,
                  alt: "插件图标"
                }, null, 8, _hoisted_12$1)
              ])
            ]),
            _: 1
          })) : vue.createCommentVNode("", true),
          vue.createVNode(_component_n_card, {
            title: "分类和标签",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_space, null, {
                default: vue.withCtx(() => [
                  __props.plugin.category ? (vue.openBlock(), vue.createBlock(_component_n_tag, {
                    key: 0,
                    type: "info"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(" 分类: " + vue.toDisplayString(__props.plugin.category), 1)
                    ]),
                    _: 1
                  })) : vue.createCommentVNode("", true),
                  (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(__props.plugin.tags, (tag) => {
                    return vue.openBlock(), vue.createBlock(_component_n_tag, {
                      key: tag,
                      type: "default"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createTextVNode(vue.toDisplayString(tag), 1)
                      ]),
                      _: 2
                    }, 1024);
                  }), 128)),
                  !__props.plugin.category && (!__props.plugin.tags || __props.plugin.tags.length === 0) ? (vue.openBlock(), vue.createElementBlock("span", _hoisted_13$1, " 暂无分类和标签 ")) : vue.createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            _: 1
          }),
          vue.createVNode(_component_n_card, {
            title: "使用统计",
            bordered: false,
            class: "detail-card"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_descriptions, {
                column: 2,
                "label-placement": "left"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_descriptions_item, { label: "安装次数" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.plugin.installCount), 1)
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_descriptions_item, { label: "活跃用户" }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(__props.plugin.activeUsers), 1)
                    ]),
                    _: 1
                  }),
                  __props.plugin.rating ? (vue.openBlock(), vue.createBlock(_component_n_descriptions_item, {
                    key: 0,
                    label: "评分"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_rate, {
                        value: __props.plugin.rating,
                        readonly: "",
                        size: "small"
                      }, null, 8, ["value"]),
                      vue.createElementVNode("span", _hoisted_14$1, vue.toDisplayString(__props.plugin.rating.toFixed(1)), 1)
                    ]),
                    _: 1
                  })) : vue.createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            _: 1
          }),
          vue.createElementVNode("div", _hoisted_15$1, [
            vue.createVNode(_component_n_space, { justify: "end" }, {
              default: vue.withCtx(() => [
                vue.createVNode(_component_n_button, {
                  onClick: _cache[0] || (_cache[0] = ($event) => _ctx.$emit("close"))
                }, {
                  default: vue.withCtx(() => [..._cache[6] || (_cache[6] = [
                    vue.createTextVNode(" 关闭 ", -1)
                  ])]),
                  _: 1
                }),
                vue.createVNode(_component_n_button, {
                  onClick: _cache[1] || (_cache[1] = ($event) => _ctx.$emit("edit", __props.plugin.pluginId))
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(CreateOutline))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[7] || (_cache[7] = vue.createTextVNode(" 编辑 ", -1))
                  ]),
                  _: 1
                }),
                __props.plugin.status === "ONLINE" ? (vue.openBlock(), vue.createBlock(_component_n_button, {
                  key: 0,
                  type: "error",
                  onClick: _cache[2] || (_cache[2] = ($event) => _ctx.$emit("delist", __props.plugin.pluginId))
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(BanOutline))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[8] || (_cache[8] = vue.createTextVNode(" 下架 ", -1))
                  ]),
                  _: 1
                })) : vue.createCommentVNode("", true),
                __props.plugin.status === "DELISTED" ? (vue.openBlock(), vue.createBlock(_component_n_button, {
                  key: 1,
                  type: "success",
                  onClick: _cache[3] || (_cache[3] = ($event) => _ctx.$emit("relist", __props.plugin.pluginId))
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(CheckmarkCircle))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[9] || (_cache[9] = vue.createTextVNode(" 重新上架 ", -1))
                  ]),
                  _: 1
                })) : vue.createCommentVNode("", true)
              ]),
              _: 1
            })
          ])
        ]);
      };
    }
  });
  const PluginDetail = /* @__PURE__ */ _export_sfc(_sfc_main$a, [["__scopeId", "data-v-3e715355"]]);
  const _hoisted_1$9 = { class: "plugin-management-page" };
  const _hoisted_2$9 = { class: "page-header-wrapper" };
  const _hoisted_3$9 = { class: "page-content" };
  const _hoisted_4$9 = { class: "table-container" };
  const _hoisted_5$9 = { class: "pagination-container" };
  const _hoisted_6$5 = { style: { "margin-bottom": "12px" } };
  const _hoisted_7$4 = { key: 0 };
  const _hoisted_8$3 = { style: { "color": "#18a058" } };
  const _hoisted_9$2 = { style: { "margin-bottom": "12px" } };
  const _sfc_main$9 = /* @__PURE__ */ vue.defineComponent({
    __name: "PluginManagement",
    setup(__props) {
      const BanOutline = useIcon("BanOutline");
      const Refresh = useIcon("RefreshOutline");
      const Eye = useIcon("EyeOutline");
      const CreateOutline = useIcon("CreateOutline");
      const CheckmarkCircle = useIcon("CheckmarkCircleOutline");
      const CloudUploadOutline = useIcon("CloudUploadOutline");
      const CloudUpload = useIcon("CloudUpload");
      const message = useMessage();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const batchRelistLoading = vue.ref(false);
      const batchDelistLoading = vue.ref(false);
      const tableData = vue.ref([]);
      const checkedRowKeys = vue.ref([]);
      const showDetailDrawer = vue.ref(false);
      const currentPlugin = vue.ref(null);
      const showAdvanced = vue.ref(false);
      const filters = vue.ref({
        status: "",
        pluginType: "",
        keyword: "",
        startTime: null,
        endTime: null
      });
      const pagination = vue.reactive({
        page: 1,
        pageSize: 20,
        itemCount: 0,
        pageSizes: [10, 20, 50, 100]
      });
      const handlePageChange = (page) => {
        pagination.page = page;
        loadData();
      };
      const handlePageSizeChange = (pageSize) => {
        pagination.pageSize = pageSize;
        pagination.page = 1;
        loadData();
      };
      const showDelistModal = vue.ref(false);
      const delistFormRef = vue.ref(null);
      const delistForm = vue.reactive({
        reason: ""
      });
      const delistRules = {
        reason: [
          { required: true, message: "请输入下架原因", trigger: "blur" }
        ]
      };
      const delistTargetIds = vue.ref([]);
      const showEditModal = vue.ref(false);
      const editFormRef = vue.ref(null);
      const editForm = vue.reactive({
        pluginId: "",
        pluginName: "",
        description: "",
        category: ""
      });
      const editRules = {
        pluginName: [
          { required: true, message: "请输入插件名称", trigger: "blur" }
        ]
      };
      const showUploadModal = vue.ref(false);
      const uploadFormRef = vue.ref(null);
      const uploading = vue.ref(false);
      const uploadProgress = vue.ref(0);
      const fileList = vue.ref([]);
      const uploadForm = vue.reactive({
        file: null,
        pluginType: null,
        description: "",
        autoList: false
      });
      const uploadRules = {
        file: [
          {
            required: true,
            message: "请选择插件包文件",
            validator: (rule, value) => {
              return !!uploadForm.file;
            },
            trigger: ["change", "blur"]
          }
        ],
        pluginType: [
          { required: true, message: "请选择插件类型", trigger: "change" }
        ]
      };
      const showUpgradeModal = vue.ref(false);
      const upgradeFormRef = vue.ref(null);
      const upgrading = vue.ref(false);
      const upgradeProgress = vue.ref(0);
      const upgradeFileList = vue.ref([]);
      const upgradeTargetPlugin = vue.ref(null);
      const upgradeForm = vue.reactive({
        parsedVersion: "",
        // 从 JAR 包解析的版本号
        file: null,
        updateNotes: "",
        autoList: false
      });
      const upgradeRules = {
        file: [
          {
            required: true,
            message: "请选择插件包文件",
            validator: (rule, value) => {
              return !!upgradeForm.file;
            },
            trigger: ["change", "blur"]
          }
        ],
        updateNotes: [
          { required: true, message: "请输入更新说明", trigger: "blur" }
        ]
      };
      const pluginTypeOptions = vue.ref([]);
      const pluginTypeMap = vue.ref({});
      const loadPluginTypes = async () => {
        const response = await pluginApi.getTypes();
        pluginTypeOptions.value = [
          { label: "全部", value: "" },
          ...response.map((type) => ({
            label: type.label,
            value: type.code
          }))
        ];
        const map = {};
        response.forEach((type) => {
          map[type.code] = type;
        });
        pluginTypeMap.value = map;
      };
      vue.computed(() => checkedRowKeys.value.length > 0);
      const hasOnlineSelection = vue.computed(() => {
        const selectedPlugins = tableData.value.filter((p) => checkedRowKeys.value.includes(p.id));
        return selectedPlugins.some((p) => p.status === "ONLINE");
      });
      const hasDelistedSelection = vue.computed(() => {
        const selectedPlugins = tableData.value.filter((p) => checkedRowKeys.value.includes(p.id));
        return selectedPlugins.some((p) => p.status === "DELISTED" || p.status === "OFFLINE");
      });
      const basicFields = vue.computed(() => [
        {
          key: "status",
          label: "插件状态",
          type: "select",
          placeholder: "请选择状态",
          clearable: true,
          options: [
            { label: "全部", value: "" },
            { label: "上架", value: "ONLINE" },
            { label: "下架", value: "OFFLINE" },
            { label: "已下架", value: "DELISTED" }
          ]
        },
        {
          key: "pluginType",
          label: "插件类型",
          type: "select",
          placeholder: "请选择类型",
          clearable: true,
          options: pluginTypeOptions.value
        },
        {
          key: "keyword",
          label: "关键词",
          type: "input",
          placeholder: "搜索插件名称、开发者"
        }
      ]);
      const advancedFields = [
        {
          key: "timeRange",
          label: "上架时间",
          type: "date-range",
          placeholder: "选择时间范围",
          span: 2
        }
      ];
      const columns = [
        {
          type: "selection"
        },
        {
          title: "插件名称",
          key: "pluginName",
          width: 180,
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "插件ID",
          key: "pluginId",
          width: 150,
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "当前版本",
          key: "currentVersion",
          width: 100
        },
        {
          title: "类型",
          key: "pluginType",
          width: 100,
          render: (row) => {
            const NTag = vue.resolveComponent("NTag");
            const typeInfo = pluginTypeMap.value[row.pluginType];
            if (typeInfo) {
              return vue.h(NTag, {
                type: typeInfo.tagType || "info",
                size: "small"
              }, { default: () => typeInfo.label });
            }
            const defaultMap = {
              TASK: { label: "任务节点", type: "info" },
              TRIGGER: { label: "触发器", type: "success" },
              APPLICATION: { label: "应用插件", type: "warning" },
              MIDDLEWARE: { label: "中间件", type: "error" }
            };
            const config = defaultMap[row.pluginType] || { label: row.pluginType, type: "info" };
            return vue.h(NTag, { type: config.type, size: "small" }, { default: () => config.label });
          }
        },
        {
          title: "开发者",
          key: "developerName",
          width: 120,
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "状态",
          key: "status",
          width: 100,
          render: (row) => {
            const NTag = vue.resolveComponent("NTag");
            const statusMap = {
              ONLINE: { label: "上架", type: "success" },
              OFFLINE: { label: "下架", type: "warning" },
              DELISTED: { label: "已下架", type: "error" }
            };
            const config = statusMap[row.status];
            return vue.h(NTag, { type: config.type, size: "small" }, { default: () => config.label });
          }
        },
        {
          title: "安装次数",
          key: "installCount",
          width: 100
        },
        {
          title: "活跃用户",
          key: "activeUsers",
          width: 100
        },
        {
          title: "上架时间",
          key: "listingTime",
          width: 160,
          render: (row) => formatDateTime(row.listingTime)
        },
        {
          title: "操作",
          key: "actions",
          width: 220,
          fixed: "right",
          render: (row) => {
            const NButton = vue.resolveComponent("NButton");
            const NSpace = vue.resolveComponent("NSpace");
            const NIcon = vue.resolveComponent("NIcon");
            const NDropdown = vue.resolveComponent("NDropdown");
            const MoreHorizontal = useIcon("EllipsisHorizontal");
            const moreOptions = [
              {
                label: "升级版本",
                key: "upgrade",
                icon: () => vue.h(NIcon, { component: CloudUpload })
              }
            ];
            if (row.status === "ONLINE") {
              moreOptions.push({
                label: "下架",
                key: "delist",
                icon: () => vue.h(NIcon, { component: BanOutline })
              });
            } else if (row.status === "DELISTED") {
              moreOptions.push({
                label: "重新上架",
                key: "relist",
                icon: () => vue.h(NIcon, { component: CheckmarkCircle })
              });
            }
            const handleMoreSelect = (key) => {
              switch (key) {
                case "upgrade":
                  handleUpgradeVersion(row.pluginId);
                  break;
                case "delist":
                  handleDelist(row.pluginId);
                  break;
                case "relist":
                  handleRelist(row.pluginId);
                  break;
              }
            };
            return vue.h(
              NSpace,
              { size: 2, wrap: false },
              {
                default: () => [
                  vue.h(
                    NButton,
                    {
                      size: "small",
                      onClick: () => handleViewDetail(row)
                    },
                    {
                      icon: () => vue.h(NIcon, { component: Eye }),
                      default: () => "查看"
                    }
                  ),
                  vue.h(
                    NButton,
                    {
                      size: "small",
                      type: "primary",
                      onClick: () => handleEdit(row.pluginId)
                    },
                    {
                      icon: () => vue.h(NIcon, { component: CreateOutline }),
                      default: () => "编辑"
                    }
                  ),
                  vue.h(
                    NDropdown,
                    {
                      trigger: "click",
                      options: moreOptions,
                      onSelect: handleMoreSelect
                    },
                    {
                      default: () => vue.h(
                        NButton,
                        {
                          size: "small",
                          quaternary: true
                        },
                        {
                          icon: () => vue.h(NIcon, { component: MoreHorizontal })
                        }
                      )
                    }
                  )
                ]
              }
            );
          }
        }
      ];
      async function loadData() {
        loading.value = true;
        refreshLoading.value = true;
        try {
          const params = {
            page: pagination.page,
            size: pagination.pageSize,
            status: filters.value.status || void 0,
            type: filters.value.pluginType || void 0,
            keyword: filters.value.keyword || void 0
          };
          const response = await pluginApi.getList(params);
          tableData.value = response.items || [];
          pagination.itemCount = response.total || 0;
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      }
      function handleViewDetail(plugin) {
        currentPlugin.value = plugin;
        showDetailDrawer.value = true;
      }
      function handleDelist(pluginId) {
        delistTargetIds.value = Array.isArray(pluginId) ? pluginId : [pluginId];
        delistForm.reason = "";
        showDelistModal.value = true;
      }
      async function confirmDelist() {
        var _a, _b, _c;
        await ((_a = delistFormRef.value) == null ? void 0 : _a.validate());
        let successCount = 0;
        let failedCount = 0;
        const errors = [];
        for (const pluginId of delistTargetIds.value) {
          try {
            await pluginApi.delist(pluginId, { reason: delistForm.reason });
            successCount++;
          } catch (error) {
            failedCount++;
            const errorMsg = ((_c = (_b = error.response) == null ? void 0 : _b.data) == null ? void 0 : _c.errorMessage) || error.message || "未知错误";
            errors.push(`${pluginId}: ${errorMsg}`);
          }
        }
        if (successCount > 0) {
          message.success(`成功下架 ${successCount} 个插件`);
        }
        if (failedCount > 0) {
          message.error(`${failedCount} 个插件下架失败：
${errors.join("\n")}`);
        }
        showDelistModal.value = false;
        checkedRowKeys.value = [];
        await loadData();
      }
      async function handleRelist(pluginId) {
        await pluginApi.relist(pluginId);
        message.success("插件已重新上架");
        await loadData();
      }
      function handleEdit(pluginId) {
        const plugin = tableData.value.find((p) => p.pluginId === pluginId);
        if (plugin) {
          editForm.pluginId = plugin.pluginId;
          editForm.pluginName = plugin.pluginName;
          editForm.description = plugin.description || "";
          editForm.category = plugin.category || "";
          showEditModal.value = true;
        }
      }
      async function confirmEdit() {
        var _a;
        await ((_a = editFormRef.value) == null ? void 0 : _a.validate());
        await pluginApi.update(editForm.pluginId, {
          pluginName: editForm.pluginName,
          description: editForm.description,
          category: editForm.category
        });
        message.success("插件信息已更新");
        showEditModal.value = false;
        await loadData();
      }
      async function handleBatchDelist() {
        const selectedPlugins = tableData.value.filter((p) => checkedRowKeys.value.includes(p.id));
        const onlinePlugins = selectedPlugins.filter((p) => p.status === "ONLINE");
        if (onlinePlugins.length === 0) {
          message.warning("请选择在线状态的插件");
          return;
        }
        batchDelistLoading.value = true;
        try {
          const pluginIds = onlinePlugins.map((p) => p.pluginId);
          handleDelist(pluginIds);
        } finally {
          batchDelistLoading.value = false;
        }
      }
      async function handleBatchRelist() {
        var _a, _b;
        const selectedPlugins = tableData.value.filter((p) => checkedRowKeys.value.includes(p.id));
        const delistedPlugins = selectedPlugins.filter((p) => p.status === "DELISTED" || p.status === "OFFLINE");
        if (delistedPlugins.length === 0) {
          message.warning("请选择已下架状态的插件");
          return;
        }
        batchRelistLoading.value = true;
        try {
          let successCount = 0;
          let failedCount = 0;
          const errors = [];
          for (const plugin of delistedPlugins) {
            try {
              const response = await pluginApi.relist(plugin.pluginId);
              successCount++;
            } catch (error) {
              failedCount++;
              const errorMsg = ((_b = (_a = error.response) == null ? void 0 : _a.data) == null ? void 0 : _b.errorMessage) || error.message || "未知错误";
              errors.push(`${plugin.pluginName}: ${errorMsg}`);
            }
          }
          if (successCount > 0) {
            message.success(`成功上架 ${successCount} 个插件`);
          }
          if (failedCount > 0) {
            message.error(`${failedCount} 个插件上架失败：
${errors.join("\n")}`);
          }
          checkedRowKeys.value = [];
          await loadData();
        } finally {
          batchRelistLoading.value = false;
        }
      }
      function handleUpgradeVersion(pluginId) {
        const plugin = tableData.value.find((p) => p.pluginId === pluginId);
        if (plugin) {
          upgradeTargetPlugin.value = plugin;
          upgradeForm.parsedVersion = "";
          upgradeForm.file = null;
          upgradeForm.updateNotes = "";
          upgradeForm.autoList = false;
          upgradeFileList.value = [];
          showUpgradeModal.value = true;
        }
      }
      function handleUpgradeFileChange(options) {
        var _a;
        upgradeFileList.value = options.fileList;
        if (options.fileList.length > 0) {
          const file = options.fileList[0].file;
          if (!file.name.endsWith(".jar")) {
            message.error("只能上传 .jar 格式的插件包文件");
            upgradeFileList.value = [];
            upgradeForm.file = null;
            upgradeForm.parsedVersion = "";
            return;
          }
          upgradeForm.file = file;
          upgradeForm.parsedVersion = "";
          (_a = upgradeFormRef.value) == null ? void 0 : _a.validate(
            () => {
            },
            (rule) => {
              return (rule == null ? void 0 : rule.key) === "file";
            }
          );
        } else {
          upgradeForm.file = null;
          upgradeForm.parsedVersion = "";
        }
      }
      async function confirmUpgrade() {
        var _a;
        await ((_a = upgradeFormRef.value) == null ? void 0 : _a.validate());
        if (!upgradeForm.file || !upgradeTargetPlugin.value) {
          message.error("请选择插件包文件");
          return false;
        }
        upgrading.value = true;
        upgradeProgress.value = 0;
        try {
          const formData = new FormData();
          formData.append("file", upgradeForm.file);
          formData.append("pluginId", upgradeTargetPlugin.value.pluginId);
          formData.append("updateNotes", upgradeForm.updateNotes);
          formData.append("autoList", String(upgradeForm.autoList));
          const progressInterval = setInterval(() => {
            if (upgradeProgress.value < 90) {
              upgradeProgress.value += 10;
            }
          }, 200);
          const response = await pluginApi.upgrade(formData, (progress) => {
            upgradeProgress.value = Math.min(progress, 95);
          });
          clearInterval(progressInterval);
          upgradeProgress.value = 100;
          message.success("插件升级成功！");
          showUpgradeModal.value = false;
          resetUpgradeForm();
          await loadData();
        } catch (error) {
          return false;
        } finally {
          upgrading.value = false;
          upgradeProgress.value = 0;
        }
      }
      function resetUpgradeForm() {
        upgradeForm.parsedVersion = "";
        upgradeForm.file = null;
        upgradeForm.updateNotes = "";
        upgradeForm.autoList = false;
        upgradeFileList.value = [];
        upgradeProgress.value = 0;
        upgradeTargetPlugin.value = null;
      }
      function handleSearch() {
        pagination.page = 1;
        loadData();
      }
      function handleReset() {
        filters.value.status = "";
        filters.value.pluginType = "";
        filters.value.keyword = "";
        filters.value.startTime = null;
        filters.value.endTime = null;
        pagination.page = 1;
        checkedRowKeys.value = [];
        loadData();
      }
      function handleCheck(keys) {
        checkedRowKeys.value = keys;
      }
      function formatDateTime(dateStr) {
        if (!dateStr) return "-";
        const date = new Date(dateStr);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit"
        });
      }
      function handleFileChange(options) {
        var _a;
        fileList.value = options.fileList;
        if (options.fileList.length > 0) {
          const file = options.fileList[0].file;
          if (!file.name.endsWith(".jar")) {
            message.error("只能上传 .jar 格式的插件包文件");
            fileList.value = [];
            uploadForm.file = null;
            return;
          }
          uploadForm.file = file;
          (_a = uploadFormRef.value) == null ? void 0 : _a.validate(
            () => {
            },
            (rule) => {
              return (rule == null ? void 0 : rule.key) === "file";
            }
          );
        } else {
          uploadForm.file = null;
        }
      }
      async function confirmUpload() {
        var _a;
        await ((_a = uploadFormRef.value) == null ? void 0 : _a.validate());
        if (!uploadForm.file) {
          message.error("请选择插件包文件");
          return false;
        }
        uploading.value = true;
        uploadProgress.value = 0;
        try {
          const formData = new FormData();
          formData.append("file", uploadForm.file);
          formData.append("pluginType", uploadForm.pluginType || "");
          formData.append("description", uploadForm.description);
          formData.append("autoList", String(uploadForm.autoList));
          const progressInterval = setInterval(() => {
            if (uploadProgress.value < 90) {
              uploadProgress.value += 10;
            }
          }, 200);
          const response = await pluginApi.upload(formData, (progress) => {
            uploadProgress.value = Math.min(progress, 95);
          });
          clearInterval(progressInterval);
          uploadProgress.value = 100;
          message.success("插件上传成功！");
          showUploadModal.value = false;
          resetUploadForm();
          await loadData();
        } catch (error) {
          return false;
        } finally {
          uploading.value = false;
          uploadProgress.value = 0;
        }
      }
      function resetUploadForm() {
        uploadForm.file = null;
        uploadForm.pluginType = null;
        uploadForm.description = "";
        uploadForm.autoList = false;
        fileList.value = [];
        uploadProgress.value = 0;
      }
      vue.onMounted(() => {
        loadPluginTypes();
        loadData();
      });
      return (_ctx, _cache) => {
        const _component_n_icon = vue.resolveComponent("n-icon");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_pagination = vue.resolveComponent("n-pagination");
        const _component_n_drawer_content = vue.resolveComponent("n-drawer-content");
        const _component_n_drawer = vue.resolveComponent("n-drawer");
        const _component_n_input = vue.resolveComponent("n-input");
        const _component_n_form_item = vue.resolveComponent("n-form-item");
        const _component_n_form = vue.resolveComponent("n-form");
        const _component_n_modal = vue.resolveComponent("n-modal");
        const _component_n_text = vue.resolveComponent("n-text");
        const _component_n_p = vue.resolveComponent("n-p");
        const _component_n_upload = vue.resolveComponent("n-upload");
        const _component_n_select = vue.resolveComponent("n-select");
        const _component_n_switch = vue.resolveComponent("n-switch");
        const _component_n_progress = vue.resolveComponent("n-progress");
        const _component_n_alert = vue.resolveComponent("n-alert");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$9, [
          vue.createElementVNode("div", _hoisted_2$9, [
            vue.createVNode(_component_PageHeader, {
              title: "插件列表管理",
              subtitle: "管理已上架的插件"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(_component_n_space, null, {
                  default: vue.withCtx(() => [
                    vue.createVNode(_component_n_button, {
                      type: "primary",
                      onClick: _cache[0] || (_cache[0] = ($event) => showUploadModal.value = true)
                    }, {
                      icon: vue.withCtx(() => [
                        vue.createVNode(_component_n_icon, null, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(CloudUploadOutline))))
                          ]),
                          _: 1
                        })
                      ]),
                      default: vue.withCtx(() => [
                        _cache[20] || (_cache[20] = vue.createTextVNode(" 上传新插件 ", -1))
                      ]),
                      _: 1
                    }),
                    vue.createVNode(_component_n_button, {
                      type: "success",
                      onClick: handleBatchRelist,
                      disabled: !hasDelistedSelection.value,
                      loading: batchRelistLoading.value
                    }, {
                      icon: vue.withCtx(() => [
                        vue.createVNode(_component_n_icon, null, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(CheckmarkCircle))))
                          ]),
                          _: 1
                        })
                      ]),
                      default: vue.withCtx(() => [
                        _cache[21] || (_cache[21] = vue.createTextVNode(" 批量上架 ", -1))
                      ]),
                      _: 1
                    }, 8, ["disabled", "loading"]),
                    vue.createVNode(_component_n_button, {
                      type: "error",
                      onClick: handleBatchDelist,
                      disabled: !hasOnlineSelection.value,
                      loading: batchDelistLoading.value
                    }, {
                      icon: vue.withCtx(() => [
                        vue.createVNode(_component_n_icon, null, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(BanOutline))))
                          ]),
                          _: 1
                        })
                      ]),
                      default: vue.withCtx(() => [
                        _cache[22] || (_cache[22] = vue.createTextVNode(" 批量下架 ", -1))
                      ]),
                      _: 1
                    }, 8, ["disabled", "loading"]),
                    vue.createVNode(_component_n_button, {
                      onClick: loadData,
                      loading: refreshLoading.value
                    }, {
                      icon: vue.withCtx(() => [
                        vue.createVNode(_component_n_icon, null, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                          ]),
                          _: 1
                        })
                      ]),
                      default: vue.withCtx(() => [
                        _cache[23] || (_cache[23] = vue.createTextVNode(" 刷新 ", -1))
                      ]),
                      _: 1
                    }, 8, ["loading"])
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$9, [
            vue.createVNode(_component_FilterPanel, {
              filters: filters.value,
              "onUpdate:filters": _cache[1] || (_cache[1] = ($event) => filters.value = $event),
              "show-advanced": showAdvanced.value,
              "onUpdate:showAdvanced": _cache[2] || (_cache[2] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields.value,
              "advanced-fields": advancedFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced", "basic-fields"]),
            vue.createElementVNode("div", _hoisted_4$9, [
              vue.createVNode(_component_n_data_table, {
                columns,
                data: tableData.value,
                loading: loading.value,
                pagination: false,
                "row-key": (row) => row.id,
                "checked-row-keys": checkedRowKeys.value,
                "onUpdate:checkedRowKeys": handleCheck,
                striped: ""
              }, null, 8, ["data", "loading", "row-key", "checked-row-keys"]),
              vue.createElementVNode("div", _hoisted_5$9, [
                vue.createVNode(_component_n_pagination, {
                  page: pagination.page,
                  "onUpdate:page": [
                    _cache[3] || (_cache[3] = ($event) => pagination.page = $event),
                    handlePageChange
                  ],
                  "page-size": pagination.pageSize,
                  "onUpdate:pageSize": [
                    _cache[4] || (_cache[4] = ($event) => pagination.pageSize = $event),
                    handlePageSizeChange
                  ],
                  "item-count": pagination.itemCount,
                  "page-sizes": pagination.pageSizes,
                  "show-size-picker": "",
                  "show-quick-jumper": ""
                }, {
                  prefix: vue.withCtx(({ itemCount }) => [
                    vue.createTextVNode(" 共 " + vue.toDisplayString(itemCount) + " 条 ", 1)
                  ]),
                  _: 1
                }, 8, ["page", "page-size", "item-count", "page-sizes"])
              ])
            ])
          ]),
          vue.createVNode(_component_n_drawer, {
            show: showDetailDrawer.value,
            "onUpdate:show": _cache[6] || (_cache[6] = ($event) => showDetailDrawer.value = $event),
            width: 720,
            placement: "right"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_drawer_content, {
                title: "插件详情",
                closable: ""
              }, {
                default: vue.withCtx(() => [
                  currentPlugin.value ? (vue.openBlock(), vue.createBlock(PluginDetail, {
                    key: 0,
                    plugin: currentPlugin.value,
                    onDelist: handleDelist,
                    onRelist: handleRelist,
                    onEdit: handleEdit,
                    onClose: _cache[5] || (_cache[5] = ($event) => showDetailDrawer.value = false)
                  }, null, 8, ["plugin"])) : vue.createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["show"]),
          vue.createVNode(_component_n_modal, {
            show: showDelistModal.value,
            "onUpdate:show": _cache[8] || (_cache[8] = ($event) => showDelistModal.value = $event),
            preset: "dialog",
            title: "下架插件",
            "positive-text": "确认下架",
            "negative-text": "取消",
            onPositiveClick: confirmDelist
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "delistFormRef",
                ref: delistFormRef,
                model: delistForm,
                rules: delistRules
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "下架原因",
                    path: "reason"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: delistForm.reason,
                        "onUpdate:value": _cache[7] || (_cache[7] = ($event) => delistForm.reason = $event),
                        type: "textarea",
                        placeholder: "请输入下架原因（必填）",
                        rows: 4
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show"]),
          vue.createVNode(_component_n_modal, {
            show: showEditModal.value,
            "onUpdate:show": _cache[12] || (_cache[12] = ($event) => showEditModal.value = $event),
            preset: "dialog",
            title: "编辑插件信息",
            "positive-text": "保存",
            "negative-text": "取消",
            onPositiveClick: confirmEdit
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "editFormRef",
                ref: editFormRef,
                model: editForm,
                rules: editRules
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "插件名称",
                    path: "pluginName"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: editForm.pluginName,
                        "onUpdate:value": _cache[9] || (_cache[9] = ($event) => editForm.pluginName = $event),
                        placeholder: "请输入插件名称"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "插件描述",
                    path: "description"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: editForm.description,
                        "onUpdate:value": _cache[10] || (_cache[10] = ($event) => editForm.description = $event),
                        type: "textarea",
                        placeholder: "请输入插件描述",
                        rows: 4
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "分类",
                    path: "category"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: editForm.category,
                        "onUpdate:value": _cache[11] || (_cache[11] = ($event) => editForm.category = $event),
                        placeholder: "请输入分类"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show"]),
          vue.createVNode(_component_n_modal, {
            show: showUploadModal.value,
            "onUpdate:show": _cache[16] || (_cache[16] = ($event) => showUploadModal.value = $event),
            preset: "dialog",
            title: "上传新插件",
            "positive-text": "开始上传",
            "negative-text": "取消",
            "positive-button-props": { disabled: !uploadForm.file },
            onPositiveClick: confirmUpload
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "uploadFormRef",
                ref: uploadFormRef,
                model: uploadForm,
                rules: uploadRules
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "插件包文件",
                    path: "file"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_upload, {
                        max: 1,
                        "default-upload": false,
                        accept: ".jar",
                        onChange: handleFileChange,
                        "file-list": fileList.value
                      }, {
                        default: vue.withCtx(() => [
                          vue.createVNode(vue.unref(naiveUi.NUploadDragger), null, {
                            default: vue.withCtx(() => [
                              vue.createElementVNode("div", _hoisted_6$5, [
                                vue.createVNode(_component_n_icon, {
                                  size: "48",
                                  depth: 3
                                }, {
                                  default: vue.withCtx(() => [
                                    (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(CloudUploadOutline))))
                                  ]),
                                  _: 1
                                })
                              ]),
                              vue.createVNode(_component_n_text, { style: { "font-size": "16px" } }, {
                                default: vue.withCtx(() => [..._cache[24] || (_cache[24] = [
                                  vue.createTextVNode(" 点击或拖拽文件到此区域上传 ", -1)
                                ])]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_p, {
                                depth: "3",
                                style: { "margin": "8px 0 0 0" }
                              }, {
                                default: vue.withCtx(() => [..._cache[25] || (_cache[25] = [
                                  vue.createTextVNode(" 仅支持 .jar 格式的插件包文件 ", -1)
                                ])]),
                                _: 1
                              })
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }, 8, ["file-list"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "插件类型",
                    path: "pluginType"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_select, {
                        value: uploadForm.pluginType,
                        "onUpdate:value": _cache[13] || (_cache[13] = ($event) => uploadForm.pluginType = $event),
                        placeholder: "请选择插件类型",
                        options: pluginTypeOptions.value
                      }, null, 8, ["value", "options"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "插件描述",
                    path: "description"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: uploadForm.description,
                        "onUpdate:value": _cache[14] || (_cache[14] = ($event) => uploadForm.description = $event),
                        type: "textarea",
                        placeholder: "请输入插件描述（可选）",
                        rows: 3
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "是否立即上架",
                    path: "autoList"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_switch, {
                        value: uploadForm.autoList,
                        "onUpdate:value": _cache[15] || (_cache[15] = ($event) => uploadForm.autoList = $event)
                      }, null, 8, ["value"]),
                      vue.createVNode(_component_n_text, {
                        depth: "3",
                        style: { "margin-left": "12px", "font-size": "14px" }
                      }, {
                        default: vue.withCtx(() => [..._cache[26] || (_cache[26] = [
                          vue.createTextVNode(" 开启后插件将自动上架到应用商店 ", -1)
                        ])]),
                        _: 1
                      })
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"]),
              uploading.value ? (vue.openBlock(), vue.createBlock(_component_n_progress, {
                key: 0,
                type: "line",
                percentage: uploadProgress.value,
                "indicator-placement": "inside",
                processing: ""
              }, null, 8, ["percentage"])) : vue.createCommentVNode("", true)
            ]),
            _: 1
          }, 8, ["show", "positive-button-props"]),
          vue.createVNode(_component_n_modal, {
            show: showUpgradeModal.value,
            "onUpdate:show": _cache[19] || (_cache[19] = ($event) => showUpgradeModal.value = $event),
            preset: "dialog",
            title: "升级插件版本",
            "positive-text": "确认升级",
            "negative-text": "取消",
            onPositiveClick: confirmUpgrade
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_alert, {
                type: "info",
                style: { "margin-bottom": "16px" }
              }, {
                header: vue.withCtx(() => [..._cache[27] || (_cache[27] = [
                  vue.createTextVNode("升级插件", -1)
                ])]),
                default: vue.withCtx(() => {
                  var _a, _b, _c;
                  return [
                    _cache[30] || (_cache[30] = vue.createTextVNode(" 正在为插件 ", -1)),
                    vue.createElementVNode("strong", null, vue.toDisplayString((_a = upgradeTargetPlugin.value) == null ? void 0 : _a.pluginName), 1),
                    vue.createTextVNode(" (" + vue.toDisplayString((_b = upgradeTargetPlugin.value) == null ? void 0 : _b.pluginId) + ") 升级新版本 ", 1),
                    _cache[31] || (_cache[31] = vue.createElementVNode("br", null, null, -1)),
                    _cache[32] || (_cache[32] = vue.createTextVNode(" 当前版本：", -1)),
                    vue.createElementVNode("strong", null, vue.toDisplayString((_c = upgradeTargetPlugin.value) == null ? void 0 : _c.currentVersion), 1),
                    upgradeForm.parsedVersion ? (vue.openBlock(), vue.createElementBlock("span", _hoisted_7$4, [
                      _cache[28] || (_cache[28] = vue.createElementVNode("br", null, null, -1)),
                      _cache[29] || (_cache[29] = vue.createTextVNode(" 检测到的新版本：", -1)),
                      vue.createElementVNode("strong", _hoisted_8$3, vue.toDisplayString(upgradeForm.parsedVersion), 1)
                    ])) : vue.createCommentVNode("", true)
                  ];
                }),
                _: 1
              }),
              vue.createVNode(_component_n_form, {
                ref_key: "upgradeFormRef",
                ref: upgradeFormRef,
                model: upgradeForm,
                rules: upgradeRules
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "插件包文件",
                    path: "file"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_upload, {
                        max: 1,
                        "default-upload": false,
                        accept: ".jar",
                        onChange: handleUpgradeFileChange,
                        "file-list": upgradeFileList.value
                      }, {
                        default: vue.withCtx(() => [
                          vue.createVNode(vue.unref(naiveUi.NUploadDragger), null, {
                            default: vue.withCtx(() => [
                              vue.createElementVNode("div", _hoisted_9$2, [
                                vue.createVNode(_component_n_icon, {
                                  size: "48",
                                  depth: 3
                                }, {
                                  default: vue.withCtx(() => [
                                    (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(CloudUploadOutline))))
                                  ]),
                                  _: 1
                                })
                              ]),
                              vue.createVNode(_component_n_text, { style: { "font-size": "16px" } }, {
                                default: vue.withCtx(() => [..._cache[33] || (_cache[33] = [
                                  vue.createTextVNode(" 点击或拖拽文件到此区域上传 ", -1)
                                ])]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_p, {
                                depth: "3",
                                style: { "margin": "8px 0 0 0" }
                              }, {
                                default: vue.withCtx(() => [..._cache[34] || (_cache[34] = [
                                  vue.createTextVNode(" 仅支持 .jar 格式的插件包文件，版本号将自动从包中解析 ", -1)
                                ])]),
                                _: 1
                              })
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }, 8, ["file-list"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "更新说明",
                    path: "updateNotes"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: upgradeForm.updateNotes,
                        "onUpdate:value": _cache[17] || (_cache[17] = ($event) => upgradeForm.updateNotes = $event),
                        type: "textarea",
                        placeholder: "请输入本次更新的内容说明",
                        rows: 4
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "是否立即上架",
                    path: "autoList"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_switch, {
                        value: upgradeForm.autoList,
                        "onUpdate:value": _cache[18] || (_cache[18] = ($event) => upgradeForm.autoList = $event)
                      }, null, 8, ["value"]),
                      vue.createVNode(_component_n_text, {
                        depth: "3",
                        style: { "margin-left": "12px", "font-size": "14px" }
                      }, {
                        default: vue.withCtx(() => [..._cache[35] || (_cache[35] = [
                          vue.createTextVNode(" 开启后新版本将自动上架到应用商店 ", -1)
                        ])]),
                        _: 1
                      })
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"]),
              upgrading.value ? (vue.openBlock(), vue.createBlock(_component_n_progress, {
                key: 0,
                type: "line",
                percentage: upgradeProgress.value,
                "indicator-placement": "inside",
                processing: ""
              }, null, 8, ["percentage"])) : vue.createCommentVNode("", true)
            ]),
            _: 1
          }, 8, ["show"])
        ]);
      };
    }
  });
  const PluginManagement = /* @__PURE__ */ _export_sfc(_sfc_main$9, [["__scopeId", "data-v-f7880059"]]);
  const _hoisted_1$8 = { class: "statistics-analysis-page" };
  const _hoisted_2$8 = { class: "page-header-wrapper" };
  const _hoisted_3$8 = { class: "page-content" };
  const _hoisted_4$8 = { class: "overview-cards" };
  const _hoisted_5$8 = { class: "stat-content" };
  const _hoisted_6$4 = {
    class: "stat-icon",
    style: { "background": "#18a058" }
  };
  const _hoisted_7$3 = { class: "stat-info" };
  const _hoisted_8$2 = { class: "stat-value" };
  const _hoisted_9$1 = { class: "stat-content" };
  const _hoisted_10 = {
    class: "stat-icon",
    style: { "background": "#2080f0" }
  };
  const _hoisted_11 = { class: "stat-info" };
  const _hoisted_12 = { class: "stat-value" };
  const _hoisted_13 = { class: "stat-content" };
  const _hoisted_14 = {
    class: "stat-icon",
    style: { "background": "#f0a020" }
  };
  const _hoisted_15 = { class: "stat-info" };
  const _hoisted_16 = { class: "stat-value" };
  const _hoisted_17 = { class: "stat-content" };
  const _hoisted_18 = {
    class: "stat-icon",
    style: { "background": "#d03050" }
  };
  const _hoisted_19 = { class: "stat-info" };
  const _hoisted_20 = { class: "stat-value" };
  const _hoisted_21 = { class: "trend-charts" };
  const _hoisted_22 = { class: "chart-container" };
  const _hoisted_23 = {
    key: 0,
    class: "simple-chart"
  };
  const _hoisted_24 = ["title"];
  const _hoisted_25 = { class: "bar-value" };
  const _hoisted_26 = {
    key: 1,
    class: "empty-chart"
  };
  const _hoisted_27 = {
    key: 0,
    class: "chart-labels"
  };
  const _hoisted_28 = { class: "chart-container" };
  const _hoisted_29 = {
    key: 0,
    class: "simple-chart"
  };
  const _hoisted_30 = ["title"];
  const _hoisted_31 = { class: "bar-value" };
  const _hoisted_32 = {
    key: 1,
    class: "empty-chart"
  };
  const _hoisted_33 = {
    key: 0,
    class: "chart-labels"
  };
  const _sfc_main$8 = /* @__PURE__ */ vue.defineComponent({
    __name: "StatisticsAnalysis",
    setup(__props) {
      const DownloadOutline = useIcon("DownloadOutline");
      const Refresh = useIcon("RefreshOutline");
      const AppsOutline = useIcon("AppsOutline");
      const PeopleOutline = useIcon("PeopleOutline");
      const TimeOutline = useIcon("TimeOutline");
      const TrophyOutline = useIcon("TrophyOutline");
      const message = useMessage();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const exportLoading = vue.ref(false);
      const dateRange = vue.ref(null);
      const quickRange = vue.ref("30d");
      const overview = vue.ref({
        totalPlugins: 0,
        totalInstalls: 0,
        totalActiveUsers: 0,
        totalDownloads: 0,
        pendingReviews: 0,
        approvedToday: 0,
        rejectedToday: 0,
        newSubmissionsToday: 0,
        topPlugins: []
      });
      const trendData = vue.ref({
        installTrend: [],
        activeTrend: [],
        downloadTrend: [],
        submissionTrend: []
      });
      const showExportModal = vue.ref(false);
      const exportFormRef = vue.ref(null);
      const exportForm = vue.reactive({
        format: "CSV",
        includeDetails: false
      });
      const exportRules = {
        format: {
          required: true,
          message: "请选择导出格式",
          trigger: "change"
        }
      };
      const formatOptions = [
        { label: "CSV", value: "CSV" },
        { label: "Excel", value: "EXCEL" },
        { label: "JSON", value: "JSON" }
      ];
      const topPluginsColumns = [
        {
          title: "排名",
          key: "rank",
          width: 80,
          render: (_row, index2) => {
            return vue.h("div", { class: "rank-badge" }, [
              vue.h(naiveUi.NIcon, {
                component: TrophyOutline,
                size: 16,
                color: index2 < 3 ? "#f0a020" : "#999"
              }),
              vue.h("span", { style: "margin-left: 4px;" }, index2 + 1)
            ]);
          }
        },
        {
          title: "插件名称",
          key: "pluginName",
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "安装次数",
          key: "installCount",
          width: 120,
          render: (row) => formatNumber(row.installCount)
        },
        {
          title: "活跃用户",
          key: "activeUsers",
          width: 120,
          render: (row) => formatNumber(row.activeUsers)
        },
        {
          title: "评分",
          key: "rating",
          width: 100,
          render: (row) => row.rating ? `${row.rating.toFixed(1)} ⭐` : "-"
        }
      ];
      const loadData = async () => {
        loading.value = true;
        refreshLoading.value = true;
        try {
          const overviewRes = await statisticsApi.getOverview();
          if (overviewRes.success && overviewRes.data) {
            overview.value = overviewRes.data;
          } else {
            console.warn("获取统计概览失败:", overviewRes.message);
          }
          const params = getDateRangeParams();
          const trendRes = await statisticsApi.getTrendData(params);
          trendData.value = trendRes;
          message.success("数据加载成功");
        } catch (error) {
          console.error("加载数据失败:", error);
          message.error(error.message || "加载数据失败");
          trendData.value = {
            installTrend: [],
            activeTrend: [],
            downloadTrend: [],
            submissionTrend: []
          };
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      };
      const getDateRangeParams = () => {
        if (dateRange.value) {
          return {
            startDate: formatDateToString(new Date(dateRange.value[0])),
            endDate: formatDateToString(new Date(dateRange.value[1]))
          };
        }
        return {};
      };
      const handleDateRangeChange = () => {
        quickRange.value = "";
        loadData();
      };
      const setQuickRange = (range) => {
        quickRange.value = range;
        const now = /* @__PURE__ */ new Date();
        const start = /* @__PURE__ */ new Date();
        switch (range) {
          case "7d":
            start.setDate(now.getDate() - 7);
            break;
          case "30d":
            start.setDate(now.getDate() - 30);
            break;
          case "90d":
            start.setDate(now.getDate() - 90);
            break;
        }
        dateRange.value = [start.getTime(), now.getTime()];
        loadData();
      };
      const handleExport = () => {
        showExportModal.value = true;
      };
      const confirmExport = async () => {
        var _a;
        try {
          await ((_a = exportFormRef.value) == null ? void 0 : _a.validate());
          exportLoading.value = true;
          const params = {
            ...exportForm,
            ...getDateRangeParams()
          };
          const res = await statisticsApi.exportStatistics(params);
          const blob = new Blob([res], { type: "text/plain" });
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement("a");
          link.href = url;
          link.download = `statistics_${Date.now()}.${exportForm.format.toLowerCase()}`;
          link.click();
          window.URL.revokeObjectURL(url);
          message.success("导出成功");
          showExportModal.value = false;
          return true;
        } catch (error) {
          if (error.errors) {
            return false;
          }
          message.error(error.message || "导出失败");
          return false;
        } finally {
          exportLoading.value = false;
        }
      };
      const formatNumber = (num) => {
        if (num >= 1e6) {
          return (num / 1e6).toFixed(1) + "M";
        }
        if (num >= 1e3) {
          return (num / 1e3).toFixed(1) + "K";
        }
        return num.toString();
      };
      const formatDate = (dateStr) => {
        const date = new Date(dateStr);
        return `${date.getMonth() + 1}/${date.getDate()}`;
      };
      const formatDateToString = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        return `${year}-${month}-${day}`;
      };
      const getBarHeight = (value, data) => {
        const max = Math.max(...data.map((d) => d.value));
        if (max === 0) return "0%";
        return `${value / max * 100}%`;
      };
      vue.onMounted(() => {
        setQuickRange("30d");
      });
      return (_ctx, _cache) => {
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_n_date_picker = vue.resolveComponent("n-date-picker");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_n_card = vue.resolveComponent("n-card");
        const _component_n_statistic = vue.resolveComponent("n-statistic");
        const _component_n_divider = vue.resolveComponent("n-divider");
        const _component_n_spin = vue.resolveComponent("n-spin");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_select = vue.resolveComponent("n-select");
        const _component_n_form_item = vue.resolveComponent("n-form-item");
        const _component_n_switch = vue.resolveComponent("n-switch");
        const _component_n_form = vue.resolveComponent("n-form");
        const _component_n_modal = vue.resolveComponent("n-modal");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$8, [
          vue.createElementVNode("div", _hoisted_2$8, [
            vue.createVNode(_component_PageHeader, {
              title: "统计分析",
              subtitle: "查看插件商店的统计数据和趋势分析"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(_component_n_button, {
                  onClick: handleExport,
                  loading: exportLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(vue.unref(naiveUi.NIcon), null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(DownloadOutline))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[7] || (_cache[7] = vue.createTextVNode(" 导出数据 ", -1))
                  ]),
                  _: 1
                }, 8, ["loading"]),
                vue.createVNode(_component_n_button, {
                  onClick: loadData,
                  loading: refreshLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(vue.unref(naiveUi.NIcon), null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[8] || (_cache[8] = vue.createTextVNode(" 刷新 ", -1))
                  ]),
                  _: 1
                }, 8, ["loading"])
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$8, [
            vue.createVNode(_component_n_card, {
              class: "filter-card",
              bordered: false
            }, {
              default: vue.withCtx(() => [
                vue.createVNode(_component_n_space, { align: "center" }, {
                  default: vue.withCtx(() => [
                    _cache[12] || (_cache[12] = vue.createElementVNode("span", { class: "filter-label" }, "时间范围：", -1)),
                    vue.createVNode(_component_n_date_picker, {
                      value: dateRange.value,
                      "onUpdate:value": [
                        _cache[0] || (_cache[0] = ($event) => dateRange.value = $event),
                        handleDateRangeChange
                      ],
                      type: "daterange",
                      clearable: ""
                    }, null, 8, ["value"]),
                    vue.createVNode(vue.unref(naiveUi.NButtonGroup), null, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_button, {
                          type: quickRange.value === "7d" ? "primary" : "default",
                          onClick: _cache[1] || (_cache[1] = ($event) => setQuickRange("7d"))
                        }, {
                          default: vue.withCtx(() => [..._cache[9] || (_cache[9] = [
                            vue.createTextVNode(" 最近7天 ", -1)
                          ])]),
                          _: 1
                        }, 8, ["type"]),
                        vue.createVNode(_component_n_button, {
                          type: quickRange.value === "30d" ? "primary" : "default",
                          onClick: _cache[2] || (_cache[2] = ($event) => setQuickRange("30d"))
                        }, {
                          default: vue.withCtx(() => [..._cache[10] || (_cache[10] = [
                            vue.createTextVNode(" 最近30天 ", -1)
                          ])]),
                          _: 1
                        }, 8, ["type"]),
                        vue.createVNode(_component_n_button, {
                          type: quickRange.value === "90d" ? "primary" : "default",
                          onClick: _cache[3] || (_cache[3] = ($event) => setQuickRange("90d"))
                        }, {
                          default: vue.withCtx(() => [..._cache[11] || (_cache[11] = [
                            vue.createTextVNode(" 最近90天 ", -1)
                          ])]),
                          _: 1
                        }, 8, ["type"])
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                })
              ]),
              _: 1
            }),
            vue.createElementVNode("div", _hoisted_4$8, [
              vue.createVNode(_component_n_card, {
                class: "stat-card",
                bordered: false
              }, {
                default: vue.withCtx(() => [
                  vue.createElementVNode("div", _hoisted_5$8, [
                    vue.createElementVNode("div", _hoisted_6$4, [
                      vue.createVNode(vue.unref(naiveUi.NIcon), { size: 32 }, {
                        default: vue.withCtx(() => [
                          (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(AppsOutline))))
                        ]),
                        _: 1
                      })
                    ]),
                    vue.createElementVNode("div", _hoisted_7$3, [
                      _cache[13] || (_cache[13] = vue.createElementVNode("div", { class: "stat-label" }, "插件总数", -1)),
                      vue.createElementVNode("div", _hoisted_8$2, vue.toDisplayString(overview.value.totalPlugins), 1)
                    ])
                  ])
                ]),
                _: 1
              }),
              vue.createVNode(_component_n_card, {
                class: "stat-card",
                bordered: false
              }, {
                default: vue.withCtx(() => [
                  vue.createElementVNode("div", _hoisted_9$1, [
                    vue.createElementVNode("div", _hoisted_10, [
                      vue.createVNode(vue.unref(naiveUi.NIcon), { size: 32 }, {
                        default: vue.withCtx(() => [
                          (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(DownloadOutline))))
                        ]),
                        _: 1
                      })
                    ]),
                    vue.createElementVNode("div", _hoisted_11, [
                      _cache[14] || (_cache[14] = vue.createElementVNode("div", { class: "stat-label" }, "总安装次数", -1)),
                      vue.createElementVNode("div", _hoisted_12, vue.toDisplayString(formatNumber(overview.value.totalInstalls)), 1)
                    ])
                  ])
                ]),
                _: 1
              }),
              vue.createVNode(_component_n_card, {
                class: "stat-card",
                bordered: false
              }, {
                default: vue.withCtx(() => [
                  vue.createElementVNode("div", _hoisted_13, [
                    vue.createElementVNode("div", _hoisted_14, [
                      vue.createVNode(vue.unref(naiveUi.NIcon), { size: 32 }, {
                        default: vue.withCtx(() => [
                          (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(PeopleOutline))))
                        ]),
                        _: 1
                      })
                    ]),
                    vue.createElementVNode("div", _hoisted_15, [
                      _cache[15] || (_cache[15] = vue.createElementVNode("div", { class: "stat-label" }, "活跃用户数", -1)),
                      vue.createElementVNode("div", _hoisted_16, vue.toDisplayString(formatNumber(overview.value.totalActiveUsers)), 1)
                    ])
                  ])
                ]),
                _: 1
              }),
              vue.createVNode(_component_n_card, {
                class: "stat-card",
                bordered: false
              }, {
                default: vue.withCtx(() => [
                  vue.createElementVNode("div", _hoisted_17, [
                    vue.createElementVNode("div", _hoisted_18, [
                      vue.createVNode(vue.unref(naiveUi.NIcon), { size: 32 }, {
                        default: vue.withCtx(() => [
                          (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(TimeOutline))))
                        ]),
                        _: 1
                      })
                    ]),
                    vue.createElementVNode("div", _hoisted_19, [
                      _cache[16] || (_cache[16] = vue.createElementVNode("div", { class: "stat-label" }, "待审核", -1)),
                      vue.createElementVNode("div", _hoisted_20, vue.toDisplayString(overview.value.pendingReviews), 1)
                    ])
                  ])
                ]),
                _: 1
              })
            ]),
            vue.createVNode(_component_n_card, {
              title: "今日数据",
              class: "today-stats",
              bordered: false
            }, {
              default: vue.withCtx(() => [
                vue.createVNode(_component_n_space, null, {
                  default: vue.withCtx(() => [
                    vue.createVNode(_component_n_statistic, {
                      label: "新提交",
                      value: overview.value.newSubmissionsToday
                    }, {
                      suffix: vue.withCtx(() => [..._cache[17] || (_cache[17] = [
                        vue.createElementVNode("span", { class: "stat-unit" }, "个", -1)
                      ])]),
                      _: 1
                    }, 8, ["value"]),
                    vue.createVNode(_component_n_divider, { vertical: "" }),
                    vue.createVNode(_component_n_statistic, {
                      label: "已批准",
                      value: overview.value.approvedToday
                    }, {
                      suffix: vue.withCtx(() => [..._cache[18] || (_cache[18] = [
                        vue.createElementVNode("span", { class: "stat-unit" }, "个", -1)
                      ])]),
                      _: 1
                    }, 8, ["value"]),
                    vue.createVNode(_component_n_divider, { vertical: "" }),
                    vue.createVNode(_component_n_statistic, {
                      label: "已拒绝",
                      value: overview.value.rejectedToday
                    }, {
                      suffix: vue.withCtx(() => [..._cache[19] || (_cache[19] = [
                        vue.createElementVNode("span", { class: "stat-unit" }, "个", -1)
                      ])]),
                      _: 1
                    }, 8, ["value"])
                  ]),
                  _: 1
                })
              ]),
              _: 1
            }),
            vue.createElementVNode("div", _hoisted_21, [
              vue.createVNode(_component_n_card, {
                title: "安装趋势",
                class: "chart-card",
                bordered: false
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_spin, { show: loading.value }, {
                    default: vue.withCtx(() => [
                      vue.createElementVNode("div", _hoisted_22, [
                        trendData.value.installTrend && trendData.value.installTrend.length > 0 ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_23, [
                          (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(trendData.value.installTrend, (point, index2) => {
                            return vue.openBlock(), vue.createElementBlock("div", {
                              key: index2,
                              class: "chart-bar",
                              style: vue.normalizeStyle({ height: getBarHeight(point.value, trendData.value.installTrend) }),
                              title: `${point.date}: ${point.value}`
                            }, [
                              vue.createElementVNode("div", _hoisted_25, vue.toDisplayString(formatNumber(point.value)), 1)
                            ], 12, _hoisted_24);
                          }), 128))
                        ])) : (vue.openBlock(), vue.createElementBlock("div", _hoisted_26, "暂无数据"))
                      ]),
                      trendData.value.installTrend && trendData.value.installTrend.length > 0 ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_27, [
                        (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(trendData.value.installTrend, (point, index2) => {
                          return vue.openBlock(), vue.createElementBlock("span", {
                            key: index2,
                            class: "chart-label"
                          }, vue.toDisplayString(formatDate(point.date)), 1);
                        }), 128))
                      ])) : vue.createCommentVNode("", true)
                    ]),
                    _: 1
                  }, 8, ["show"])
                ]),
                _: 1
              }),
              vue.createVNode(_component_n_card, {
                title: "活跃用户趋势",
                class: "chart-card",
                bordered: false
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_spin, { show: loading.value }, {
                    default: vue.withCtx(() => [
                      vue.createElementVNode("div", _hoisted_28, [
                        trendData.value.activeTrend && trendData.value.activeTrend.length > 0 ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_29, [
                          (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(trendData.value.activeTrend, (point, index2) => {
                            return vue.openBlock(), vue.createElementBlock("div", {
                              key: index2,
                              class: "chart-bar",
                              style: vue.normalizeStyle({ height: getBarHeight(point.value, trendData.value.activeTrend) }),
                              title: `${point.date}: ${point.value}`
                            }, [
                              vue.createElementVNode("div", _hoisted_31, vue.toDisplayString(formatNumber(point.value)), 1)
                            ], 12, _hoisted_30);
                          }), 128))
                        ])) : (vue.openBlock(), vue.createElementBlock("div", _hoisted_32, "暂无数据"))
                      ]),
                      trendData.value.activeTrend && trendData.value.activeTrend.length > 0 ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_33, [
                        (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(trendData.value.activeTrend, (point, index2) => {
                          return vue.openBlock(), vue.createElementBlock("span", {
                            key: index2,
                            class: "chart-label"
                          }, vue.toDisplayString(formatDate(point.date)), 1);
                        }), 128))
                      ])) : vue.createCommentVNode("", true)
                    ]),
                    _: 1
                  }, 8, ["show"])
                ]),
                _: 1
              })
            ]),
            vue.createVNode(_component_n_card, {
              title: "热门插件 TOP 10",
              class: "top-plugins",
              bordered: false
            }, {
              default: vue.withCtx(() => [
                vue.createVNode(_component_n_data_table, {
                  columns: topPluginsColumns,
                  data: overview.value.topPlugins || [],
                  pagination: false,
                  loading: loading.value,
                  striped: ""
                }, null, 8, ["data", "loading"])
              ]),
              _: 1
            }),
            vue.createVNode(_component_n_modal, {
              show: showExportModal.value,
              "onUpdate:show": _cache[6] || (_cache[6] = ($event) => showExportModal.value = $event),
              preset: "dialog",
              title: "导出统计数据",
              "positive-text": "导出",
              "negative-text": "取消",
              "positive-button-props": { loading: exportLoading.value },
              onPositiveClick: confirmExport
            }, {
              default: vue.withCtx(() => [
                vue.createVNode(_component_n_form, {
                  ref_key: "exportFormRef",
                  ref: exportFormRef,
                  model: exportForm,
                  rules: exportRules
                }, {
                  default: vue.withCtx(() => [
                    vue.createVNode(_component_n_form_item, {
                      label: "导出格式",
                      path: "format"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_select, {
                          value: exportForm.format,
                          "onUpdate:value": _cache[4] || (_cache[4] = ($event) => exportForm.format = $event),
                          options: formatOptions,
                          placeholder: "请选择导出格式"
                        }, null, 8, ["value"])
                      ]),
                      _: 1
                    }),
                    vue.createVNode(_component_n_form_item, { label: "包含详细数据" }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_switch, {
                          value: exportForm.includeDetails,
                          "onUpdate:value": _cache[5] || (_cache[5] = ($event) => exportForm.includeDetails = $event)
                        }, null, 8, ["value"])
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }, 8, ["model"])
              ]),
              _: 1
            }, 8, ["show", "positive-button-props"])
          ])
        ]);
      };
    }
  });
  const StatisticsAnalysis = /* @__PURE__ */ _export_sfc(_sfc_main$8, [["__scopeId", "data-v-cb00e5b0"]]);
  const _hoisted_1$7 = { class: "category-management-page" };
  const _hoisted_2$7 = { class: "page-header-wrapper" };
  const _hoisted_3$7 = { class: "page-content" };
  const _hoisted_4$7 = { class: "table-container" };
  const _hoisted_5$7 = {
    key: 0,
    class: "category-detail"
  };
  const _sfc_main$7 = /* @__PURE__ */ vue.defineComponent({
    __name: "CategoryManagement",
    setup(__props) {
      const Add = useIcon("AddOutline");
      const Refresh = useIcon("RefreshOutline");
      const Edit = useIcon("CreateOutline");
      const Delete = useIcon("TrashOutline");
      const message = useMessage();
      const dialog = naiveUi.useDialog();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const submitting = vue.ref(false);
      const categories = vue.ref([]);
      const showFormModal = vue.ref(false);
      const showDetailDrawer = vue.ref(false);
      const isEditing = vue.ref(false);
      const currentCategory = vue.ref(null);
      const formRef = vue.ref(null);
      const showAdvanced = vue.ref(false);
      const filters = vue.ref({
        keyword: "",
        enabled: null
      });
      const categoryForm = vue.reactive({
        categoryName: "",
        categoryKey: "",
        description: "",
        icon: "",
        displayOrder: 0,
        enabled: true
      });
      const formRules = {
        categoryName: [
          { required: true, message: "请输入分类名称", trigger: "blur" },
          { min: 2, max: 64, message: "分类名称长度为 2-64 个字符", trigger: "blur" }
        ],
        categoryKey: [
          { required: true, message: "请输入分类标识", trigger: "blur" },
          { pattern: /^[a-z0-9_-]+$/, message: "分类标识只能包含小写字母、数字、下划线和连字符", trigger: "blur" }
        ],
        displayOrder: [
          { required: true, type: "number", message: "请输入排序顺序", trigger: "blur" }
        ]
      };
      const basicFields = [
        {
          key: "keyword",
          label: "关键词",
          type: "input",
          placeholder: "请输入分类名称或标识",
          span: 12
        },
        {
          key: "enabled",
          label: "启用状态",
          type: "select",
          placeholder: "请选择启用状态",
          options: [
            { label: "全部", value: null },
            { label: "已启用", value: true },
            { label: "已禁用", value: false }
          ],
          span: 12
        }
      ];
      const columns = [
        {
          title: "ID",
          key: "id",
          width: 80
        },
        {
          title: "分类名称",
          key: "categoryName",
          width: 150
        },
        {
          title: "分类标识",
          key: "categoryKey",
          width: 150
        },
        {
          title: "图标",
          key: "icon",
          width: 120,
          render: (row) => row.icon || "-"
        },
        {
          title: "排序顺序",
          key: "displayOrder",
          width: 100
        },
        {
          title: "启用状态",
          key: "enabled",
          width: 100,
          render: (row) => vue.h(
            "n-tag",
            { type: row.enabled ? "success" : "default" },
            { default: () => row.enabled ? "已启用" : "已禁用" }
          )
        },
        {
          title: "插件数量",
          key: "pluginCount",
          width: 100
        },
        {
          title: "创建时间",
          key: "createTime",
          width: 180,
          render: (row) => formatDateTime(row.createTime)
        },
        {
          title: "操作",
          key: "actions",
          width: 150,
          fixed: "right",
          render: (row) => {
            const NButton = vue.resolveComponent("NButton");
            const NSpace = vue.resolveComponent("NSpace");
            const NIcon = vue.resolveComponent("NIcon");
            const NDropdown = vue.resolveComponent("NDropdown");
            const MoreHorizontal = useIcon("EllipsisHorizontal");
            const moreOptions = [
              {
                label: "编辑",
                key: "edit",
                icon: () => vue.h(NIcon, { component: Edit })
              },
              {
                label: "删除",
                key: "delete",
                icon: () => vue.h(NIcon, { component: Delete })
              }
            ];
            const handleMoreSelect = (key) => {
              switch (key) {
                case "edit":
                  handleEdit(row);
                  break;
                case "delete":
                  handleDelete(row);
                  break;
              }
            };
            return vue.h(
              NSpace,
              { size: 2, wrap: false },
              {
                default: () => [
                  vue.h(
                    NButton,
                    {
                      size: "small",
                      onClick: () => handleViewDetail(row.id)
                    },
                    {
                      icon: () => vue.h(NIcon, { component: useIcon("EyeOutline") }),
                      default: () => "查看"
                    }
                  ),
                  vue.h(
                    NDropdown,
                    {
                      trigger: "click",
                      options: moreOptions,
                      onSelect: handleMoreSelect
                    },
                    {
                      default: () => vue.h(
                        NButton,
                        {
                          size: "small",
                          quaternary: true
                        },
                        {
                          icon: () => vue.h(NIcon, { component: MoreHorizontal })
                        }
                      )
                    }
                  )
                ]
              }
            );
          }
        }
      ];
      const loadCategories = async () => {
        loading.value = true;
        refreshLoading.value = true;
        try {
          const response = await categoryApi.getAll();
          let filteredCategories = response;
          if (filters.value.keyword) {
            const keyword = filters.value.keyword.toLowerCase();
            filteredCategories = filteredCategories.filter(
              (cat) => cat.categoryName.toLowerCase().includes(keyword) || cat.categoryKey.toLowerCase().includes(keyword)
            );
          }
          if (filters.value.enabled !== null) {
            filteredCategories = filteredCategories.filter((cat) => cat.enabled === filters.value.enabled);
          }
          filteredCategories.sort((a, b) => a.displayOrder - b.displayOrder);
          categories.value = filteredCategories;
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      };
      const handleSearch = () => {
        loadCategories();
      };
      const handleReset = () => {
        filters.value.keyword = "";
        filters.value.enabled = null;
        loadCategories();
      };
      const handleCreate = () => {
        isEditing.value = false;
        currentCategory.value = null;
        resetForm();
        showFormModal.value = true;
      };
      const handleEdit = (category) => {
        isEditing.value = true;
        currentCategory.value = category;
        categoryForm.categoryName = category.categoryName;
        categoryForm.categoryKey = category.categoryKey;
        categoryForm.description = category.description || "";
        categoryForm.icon = category.icon || "";
        categoryForm.displayOrder = category.displayOrder;
        categoryForm.enabled = category.enabled;
        showFormModal.value = true;
      };
      const handleViewDetail = async (id) => {
        const response = await categoryApi.getById(id);
        currentCategory.value = response;
        showDetailDrawer.value = true;
      };
      const handleSubmit = async () => {
        if (!formRef.value) return;
        try {
          await formRef.value.validate();
        } catch (error) {
          return;
        }
        submitting.value = true;
        try {
          let response;
          if (isEditing.value && currentCategory.value) {
            response = await categoryApi.update(currentCategory.value.id, categoryForm);
          } else {
            response = await categoryApi.create(categoryForm);
          }
          message.success(isEditing.value ? "更新成功" : "创建成功");
          showFormModal.value = false;
          loadCategories();
        } finally {
          submitting.value = false;
        }
      };
      const handleDelete = (category) => {
        dialog.warning({
          title: "删除分类",
          content: `确定要删除分类 "${category.categoryName}" 吗？

删除后无法恢复，且该分类下的插件将失去分类信息。`,
          positiveText: "删除",
          negativeText: "取消",
          onPositiveClick: async () => {
            await categoryApi.delete(category.id);
            message.success("删除成功");
            loadCategories();
          }
        });
      };
      const resetForm = () => {
        categoryForm.categoryName = "";
        categoryForm.categoryKey = "";
        categoryForm.description = "";
        categoryForm.icon = "";
        categoryForm.displayOrder = 0;
        categoryForm.enabled = true;
      };
      const formatDateTime = (dateTime) => {
        if (!dateTime) return "-";
        const date = new Date(dateTime);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit"
        });
      };
      vue.onMounted(() => {
        loadCategories();
      });
      return (_ctx, _cache) => {
        const _component_n_icon = vue.resolveComponent("n-icon");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_input = vue.resolveComponent("n-input");
        const _component_n_form_item = vue.resolveComponent("n-form-item");
        const _component_n_input_number = vue.resolveComponent("n-input-number");
        const _component_n_switch = vue.resolveComponent("n-switch");
        const _component_n_form = vue.resolveComponent("n-form");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_n_modal = vue.resolveComponent("n-modal");
        const _component_n_descriptions_item = vue.resolveComponent("n-descriptions-item");
        const _component_n_tag = vue.resolveComponent("n-tag");
        const _component_n_descriptions = vue.resolveComponent("n-descriptions");
        const _component_n_divider = vue.resolveComponent("n-divider");
        const _component_n_card = vue.resolveComponent("n-card");
        const _component_n_drawer_content = vue.resolveComponent("n-drawer-content");
        const _component_n_drawer = vue.resolveComponent("n-drawer");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$7, [
          vue.createElementVNode("div", _hoisted_2$7, [
            vue.createVNode(_component_PageHeader, {
              title: "分类管理",
              subtitle: "管理插件分类，维护分类信息"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(_component_n_button, {
                  type: "primary",
                  onClick: handleCreate
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Add))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[12] || (_cache[12] = vue.createTextVNode(" 新建分类 ", -1))
                  ]),
                  _: 1
                }),
                vue.createVNode(_component_n_button, {
                  onClick: loadCategories,
                  loading: refreshLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[13] || (_cache[13] = vue.createTextVNode(" 刷新 ", -1))
                  ]),
                  _: 1
                }, 8, ["loading"])
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$7, [
            vue.createVNode(_component_FilterPanel, {
              filters: filters.value,
              "onUpdate:filters": _cache[0] || (_cache[0] = ($event) => filters.value = $event),
              "show-advanced": showAdvanced.value,
              "onUpdate:showAdvanced": _cache[1] || (_cache[1] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced"]),
            vue.createElementVNode("div", _hoisted_4$7, [
              vue.createVNode(_component_n_data_table, {
                columns,
                data: categories.value,
                loading: loading.value,
                "row-key": (row) => row.id,
                striped: ""
              }, null, 8, ["data", "loading", "row-key"])
            ])
          ]),
          vue.createVNode(_component_n_modal, {
            show: showFormModal.value,
            "onUpdate:show": _cache[9] || (_cache[9] = ($event) => showFormModal.value = $event),
            "mask-closable": false,
            preset: "card",
            title: isEditing.value ? "编辑分类" : "新建分类",
            style: { "width": "600px" }
          }, {
            footer: vue.withCtx(() => [
              vue.createVNode(_component_n_space, { justify: "end" }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_button, {
                    onClick: _cache[8] || (_cache[8] = ($event) => showFormModal.value = false)
                  }, {
                    default: vue.withCtx(() => [..._cache[14] || (_cache[14] = [
                      vue.createTextVNode("取消", -1)
                    ])]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_button, {
                    type: "primary",
                    onClick: handleSubmit,
                    loading: submitting.value
                  }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(isEditing.value ? "更新" : "创建"), 1)
                    ]),
                    _: 1
                  }, 8, ["loading"])
                ]),
                _: 1
              })
            ]),
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "formRef",
                ref: formRef,
                model: categoryForm,
                rules: formRules,
                "label-placement": "left",
                "label-width": "100px"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "分类名称",
                    path: "categoryName"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: categoryForm.categoryName,
                        "onUpdate:value": _cache[2] || (_cache[2] = ($event) => categoryForm.categoryName = $event),
                        placeholder: "请输入分类名称",
                        maxlength: "64",
                        "show-count": ""
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "分类标识",
                    path: "categoryKey"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: categoryForm.categoryKey,
                        "onUpdate:value": _cache[3] || (_cache[3] = ($event) => categoryForm.categoryKey = $event),
                        placeholder: "请输入分类标识（英文，用于代码中引用）",
                        maxlength: "64",
                        "show-count": ""
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "分类描述",
                    path: "description"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: categoryForm.description,
                        "onUpdate:value": _cache[4] || (_cache[4] = ($event) => categoryForm.description = $event),
                        type: "textarea",
                        placeholder: "请输入分类描述",
                        rows: 3,
                        maxlength: "500",
                        "show-count": ""
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "图标",
                    path: "icon"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: categoryForm.icon,
                        "onUpdate:value": _cache[5] || (_cache[5] = ($event) => categoryForm.icon = $event),
                        placeholder: "请输入图标名称（如：folder-outline）",
                        maxlength: "128"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "排序顺序",
                    path: "displayOrder"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input_number, {
                        value: categoryForm.displayOrder,
                        "onUpdate:value": _cache[6] || (_cache[6] = ($event) => categoryForm.displayOrder = $event),
                        min: 0,
                        max: 9999,
                        placeholder: "数字越小越靠前",
                        style: { "width": "100%" }
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "启用状态",
                    path: "enabled"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_switch, {
                        value: categoryForm.enabled,
                        "onUpdate:value": _cache[7] || (_cache[7] = ($event) => categoryForm.enabled = $event)
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show", "title"]),
          vue.createVNode(_component_n_drawer, {
            show: showDetailDrawer.value,
            "onUpdate:show": _cache[11] || (_cache[11] = ($event) => showDetailDrawer.value = $event),
            width: 600,
            placement: "right"
          }, {
            default: vue.withCtx(() => {
              var _a;
              return [
                vue.createVNode(_component_n_drawer_content, {
                  title: `分类详情 - ${((_a = currentCategory.value) == null ? void 0 : _a.categoryName) || ""}`
                }, {
                  footer: vue.withCtx(() => [
                    vue.createVNode(_component_n_space, { justify: "end" }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_button, {
                          onClick: _cache[10] || (_cache[10] = ($event) => showDetailDrawer.value = false)
                        }, {
                          default: vue.withCtx(() => [..._cache[15] || (_cache[15] = [
                            vue.createTextVNode("关闭", -1)
                          ])]),
                          _: 1
                        })
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    currentCategory.value ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_5$7, [
                      vue.createVNode(_component_n_card, {
                        title: "基本信息",
                        bordered: false,
                        class: "detail-section"
                      }, {
                        default: vue.withCtx(() => [
                          vue.createVNode(_component_n_descriptions, {
                            column: 2,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(_component_n_descriptions_item, { label: "ID" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentCategory.value.id), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "分类名称" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentCategory.value.categoryName), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "分类标识" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentCategory.value.categoryKey), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "图标" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentCategory.value.icon || "-"), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "排序顺序" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentCategory.value.displayOrder), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "启用状态" }, {
                                default: vue.withCtx(() => [
                                  vue.createVNode(_component_n_tag, {
                                    type: currentCategory.value.enabled ? "success" : "default"
                                  }, {
                                    default: vue.withCtx(() => [
                                      vue.createTextVNode(vue.toDisplayString(currentCategory.value.enabled ? "已启用" : "已禁用"), 1)
                                    ]),
                                    _: 1
                                  }, 8, ["type"])
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "插件数量" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentCategory.value.pluginCount), 1)
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          }),
                          vue.createVNode(_component_n_divider),
                          vue.createVNode(_component_n_descriptions, {
                            column: 1,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(_component_n_descriptions_item, { label: "分类描述" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentCategory.value.description || "-"), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "创建时间" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(formatDateTime(currentCategory.value.createTime)), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "更新时间" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(formatDateTime(currentCategory.value.updateTime)), 1)
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      })
                    ])) : vue.createCommentVNode("", true)
                  ]),
                  _: 1
                }, 8, ["title"])
              ];
            }),
            _: 1
          }, 8, ["show"])
        ]);
      };
    }
  });
  const CategoryManagement = /* @__PURE__ */ _export_sfc(_sfc_main$7, [["__scopeId", "data-v-cb59b69e"]]);
  const _hoisted_1$6 = { class: "tag-management-page" };
  const _hoisted_2$6 = { class: "page-header-wrapper" };
  const _hoisted_3$6 = { class: "page-content" };
  const _hoisted_4$6 = { class: "table-container" };
  const _hoisted_5$6 = {
    key: 0,
    class: "tag-detail"
  };
  const _sfc_main$6 = /* @__PURE__ */ vue.defineComponent({
    __name: "TagManagement",
    setup(__props) {
      const Add = useIcon("AddOutline");
      const Refresh = useIcon("RefreshOutline");
      const Edit = useIcon("CreateOutline");
      const Delete = useIcon("TrashOutline");
      const Eye = useIcon("EyeOutline");
      const message = useMessage();
      const dialog = naiveUi.useDialog();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const submitting = vue.ref(false);
      const tags = vue.ref([]);
      const showFormModal = vue.ref(false);
      const showDetailDrawer = vue.ref(false);
      const isEditing = vue.ref(false);
      const currentTag = vue.ref(null);
      const formRef = vue.ref(null);
      const showAdvanced = vue.ref(false);
      const filters = vue.ref({
        keyword: "",
        enabled: null
      });
      const tagForm = vue.reactive({
        tagName: "",
        tagKey: "",
        description: "",
        color: "#18a058",
        enabled: true
      });
      const formRules = {
        tagName: [
          { required: true, message: "请输入标签名称", trigger: "blur" },
          { min: 2, max: 64, message: "标签名称长度为 2-64 个字符", trigger: "blur" }
        ],
        tagKey: [
          { required: true, message: "请输入标签标识", trigger: "blur" },
          { pattern: /^[a-z0-9_-]+$/, message: "标签标识只能包含小写字母、数字、下划线和连字符", trigger: "blur" }
        ]
      };
      const basicFields = [
        {
          key: "keyword",
          label: "关键词",
          type: "input",
          placeholder: "请输入标签名称或标识",
          span: 12
        },
        {
          key: "enabled",
          label: "启用状态",
          type: "select",
          placeholder: "请选择启用状态",
          options: [
            { label: "全部", value: null },
            { label: "已启用", value: true },
            { label: "已禁用", value: false }
          ],
          span: 12
        }
      ];
      const columns = [
        {
          title: "ID",
          key: "id",
          width: 80
        },
        {
          title: "标签名称",
          key: "tagName",
          width: 150
        },
        {
          title: "标签标识",
          key: "tagKey",
          width: 150
        },
        {
          title: "标签颜色",
          key: "color",
          width: 120,
          render: (row) => {
            if (!row.color) return "-";
            return vue.h("div", {
              style: {
                display: "flex",
                alignItems: "center",
                gap: "8px"
              }
            }, [
              vue.h("div", {
                style: {
                  width: "20px",
                  height: "20px",
                  borderRadius: "4px",
                  backgroundColor: row.color,
                  border: "1px solid #ddd"
                }
              }),
              vue.h("span", row.color)
            ]);
          }
        },
        {
          title: "启用状态",
          key: "enabled",
          width: 100,
          render: (row) => vue.h(
            "n-tag",
            { type: row.enabled ? "success" : "default" },
            { default: () => row.enabled ? "已启用" : "已禁用" }
          )
        },
        {
          title: "使用次数",
          key: "usageCount",
          width: 100
        },
        {
          title: "创建时间",
          key: "createTime",
          width: 180,
          render: (row) => formatDateTime(row.createTime)
        },
        {
          title: "操作",
          key: "actions",
          width: 150,
          fixed: "right",
          render: (row) => {
            const NButton = vue.resolveComponent("NButton");
            const NSpace = vue.resolveComponent("NSpace");
            const NIcon = vue.resolveComponent("NIcon");
            const NDropdown = vue.resolveComponent("NDropdown");
            const MoreHorizontal = useIcon("EllipsisHorizontal");
            const moreOptions = [
              {
                label: "编辑",
                key: "edit",
                icon: () => vue.h(NIcon, { component: Edit })
              },
              {
                label: "删除",
                key: "delete",
                icon: () => vue.h(NIcon, { component: Delete })
              }
            ];
            const handleMoreSelect = (key) => {
              switch (key) {
                case "edit":
                  handleEdit(row);
                  break;
                case "delete":
                  handleDelete(row);
                  break;
              }
            };
            return vue.h(
              NSpace,
              { size: 2, wrap: false },
              {
                default: () => [
                  vue.h(
                    NButton,
                    {
                      size: "small",
                      onClick: () => handleViewDetail(row.id)
                    },
                    {
                      icon: () => vue.h(NIcon, { component: Eye }),
                      default: () => "查看"
                    }
                  ),
                  vue.h(
                    NDropdown,
                    {
                      trigger: "click",
                      options: moreOptions,
                      onSelect: handleMoreSelect
                    },
                    {
                      default: () => vue.h(
                        NButton,
                        {
                          size: "small",
                          quaternary: true
                        },
                        {
                          icon: () => vue.h(NIcon, { component: MoreHorizontal })
                        }
                      )
                    }
                  )
                ]
              }
            );
          }
        }
      ];
      const loadTags = async () => {
        loading.value = true;
        refreshLoading.value = true;
        try {
          const response = await tagApi.getAll();
          let filteredTags = response;
          if (filters.value.keyword) {
            const keyword = filters.value.keyword.toLowerCase();
            filteredTags = filteredTags.filter(
              (tag) => tag.tagName.toLowerCase().includes(keyword) || tag.tagKey.toLowerCase().includes(keyword)
            );
          }
          if (filters.value.enabled !== null) {
            filteredTags = filteredTags.filter((tag) => tag.enabled === filters.value.enabled);
          }
          tags.value = filteredTags;
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      };
      const handleSearch = () => {
        loadTags();
      };
      const handleReset = () => {
        filters.value.keyword = "";
        filters.value.enabled = null;
        loadTags();
      };
      const handleCreate = () => {
        isEditing.value = false;
        currentTag.value = null;
        resetForm();
        showFormModal.value = true;
      };
      const handleEdit = (tag) => {
        isEditing.value = true;
        currentTag.value = tag;
        tagForm.tagName = tag.tagName;
        tagForm.tagKey = tag.tagKey;
        tagForm.description = tag.description || "";
        tagForm.color = tag.color || "#18a058";
        tagForm.enabled = tag.enabled;
        showFormModal.value = true;
      };
      const handleViewDetail = async (id) => {
        const response = await tagApi.getById(id);
        currentTag.value = response;
        showDetailDrawer.value = true;
      };
      const handleSubmit = async () => {
        if (!formRef.value) return;
        try {
          await formRef.value.validate();
        } catch (error) {
          return;
        }
        submitting.value = true;
        try {
          let response;
          if (isEditing.value && currentTag.value) {
            response = await tagApi.update(currentTag.value.id, tagForm);
          } else {
            response = await tagApi.create(tagForm);
          }
          message.success(isEditing.value ? "更新成功" : "创建成功");
          showFormModal.value = false;
          loadTags();
        } finally {
          submitting.value = false;
        }
      };
      const handleDelete = (tag) => {
        dialog.warning({
          title: "删除标签",
          content: `确定要删除标签 "${tag.tagName}" 吗？

删除后无法恢复，且该标签将从所有插件中移除。`,
          positiveText: "删除",
          negativeText: "取消",
          onPositiveClick: async () => {
            await tagApi.delete(tag.id);
            message.success("删除成功");
            loadTags();
          }
        });
      };
      const resetForm = () => {
        tagForm.tagName = "";
        tagForm.tagKey = "";
        tagForm.description = "";
        tagForm.color = "#18a058";
        tagForm.enabled = true;
      };
      const formatDateTime = (dateTime) => {
        if (!dateTime) return "-";
        const date = new Date(dateTime);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit"
        });
      };
      vue.onMounted(() => {
        loadTags();
      });
      return (_ctx, _cache) => {
        const _component_n_icon = vue.resolveComponent("n-icon");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_input = vue.resolveComponent("n-input");
        const _component_n_form_item = vue.resolveComponent("n-form-item");
        const _component_n_color_picker = vue.resolveComponent("n-color-picker");
        const _component_n_switch = vue.resolveComponent("n-switch");
        const _component_n_form = vue.resolveComponent("n-form");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_n_modal = vue.resolveComponent("n-modal");
        const _component_n_descriptions_item = vue.resolveComponent("n-descriptions-item");
        const _component_n_tag = vue.resolveComponent("n-tag");
        const _component_n_descriptions = vue.resolveComponent("n-descriptions");
        const _component_n_divider = vue.resolveComponent("n-divider");
        const _component_n_card = vue.resolveComponent("n-card");
        const _component_n_drawer_content = vue.resolveComponent("n-drawer-content");
        const _component_n_drawer = vue.resolveComponent("n-drawer");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$6, [
          vue.createElementVNode("div", _hoisted_2$6, [
            vue.createVNode(_component_PageHeader, {
              title: "标签管理",
              subtitle: "管理插件标签，维护标签信息"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(_component_n_button, {
                  type: "primary",
                  onClick: handleCreate
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Add))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[11] || (_cache[11] = vue.createTextVNode(" 新建标签 ", -1))
                  ]),
                  _: 1
                }),
                vue.createVNode(_component_n_button, {
                  onClick: loadTags,
                  loading: refreshLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[12] || (_cache[12] = vue.createTextVNode(" 刷新 ", -1))
                  ]),
                  _: 1
                }, 8, ["loading"])
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$6, [
            vue.createVNode(_component_FilterPanel, {
              filters: filters.value,
              "onUpdate:filters": _cache[0] || (_cache[0] = ($event) => filters.value = $event),
              "show-advanced": showAdvanced.value,
              "onUpdate:showAdvanced": _cache[1] || (_cache[1] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced"]),
            vue.createElementVNode("div", _hoisted_4$6, [
              vue.createVNode(_component_n_data_table, {
                columns,
                data: tags.value,
                loading: loading.value,
                "row-key": (row) => row.id,
                striped: ""
              }, null, 8, ["data", "loading", "row-key"])
            ])
          ]),
          vue.createVNode(_component_n_modal, {
            show: showFormModal.value,
            "onUpdate:show": _cache[8] || (_cache[8] = ($event) => showFormModal.value = $event),
            "mask-closable": false,
            preset: "card",
            title: isEditing.value ? "编辑标签" : "新建标签",
            style: { "width": "600px" }
          }, {
            footer: vue.withCtx(() => [
              vue.createVNode(_component_n_space, { justify: "end" }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_button, {
                    onClick: _cache[7] || (_cache[7] = ($event) => showFormModal.value = false)
                  }, {
                    default: vue.withCtx(() => [..._cache[13] || (_cache[13] = [
                      vue.createTextVNode("取消", -1)
                    ])]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_button, {
                    type: "primary",
                    onClick: handleSubmit,
                    loading: submitting.value
                  }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(isEditing.value ? "更新" : "创建"), 1)
                    ]),
                    _: 1
                  }, 8, ["loading"])
                ]),
                _: 1
              })
            ]),
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "formRef",
                ref: formRef,
                model: tagForm,
                rules: formRules,
                "label-placement": "left",
                "label-width": "100px"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "标签名称",
                    path: "tagName"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: tagForm.tagName,
                        "onUpdate:value": _cache[2] || (_cache[2] = ($event) => tagForm.tagName = $event),
                        placeholder: "请输入标签名称",
                        maxlength: "64",
                        "show-count": ""
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "标签标识",
                    path: "tagKey"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: tagForm.tagKey,
                        "onUpdate:value": _cache[3] || (_cache[3] = ($event) => tagForm.tagKey = $event),
                        placeholder: "请输入标签标识（英文，用于代码中引用）",
                        maxlength: "64",
                        "show-count": ""
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "标签描述",
                    path: "description"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: tagForm.description,
                        "onUpdate:value": _cache[4] || (_cache[4] = ($event) => tagForm.description = $event),
                        type: "textarea",
                        placeholder: "请输入标签描述",
                        rows: 3,
                        maxlength: "500",
                        "show-count": ""
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "标签颜色",
                    path: "color"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_color_picker, {
                        value: tagForm.color,
                        "onUpdate:value": _cache[5] || (_cache[5] = ($event) => tagForm.color = $event),
                        "show-alpha": false,
                        modes: ["hex"],
                        placeholder: "选择标签颜色"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "启用状态",
                    path: "enabled"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_switch, {
                        value: tagForm.enabled,
                        "onUpdate:value": _cache[6] || (_cache[6] = ($event) => tagForm.enabled = $event)
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show", "title"]),
          vue.createVNode(_component_n_drawer, {
            show: showDetailDrawer.value,
            "onUpdate:show": _cache[10] || (_cache[10] = ($event) => showDetailDrawer.value = $event),
            width: 600,
            placement: "right"
          }, {
            default: vue.withCtx(() => {
              var _a;
              return [
                vue.createVNode(_component_n_drawer_content, {
                  title: `标签详情 - ${((_a = currentTag.value) == null ? void 0 : _a.tagName) || ""}`
                }, {
                  footer: vue.withCtx(() => [
                    vue.createVNode(_component_n_space, { justify: "end" }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_button, {
                          onClick: _cache[9] || (_cache[9] = ($event) => showDetailDrawer.value = false)
                        }, {
                          default: vue.withCtx(() => [..._cache[14] || (_cache[14] = [
                            vue.createTextVNode("关闭", -1)
                          ])]),
                          _: 1
                        })
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    currentTag.value ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_5$6, [
                      vue.createVNode(_component_n_card, {
                        title: "基本信息",
                        bordered: false,
                        class: "detail-section"
                      }, {
                        default: vue.withCtx(() => [
                          vue.createVNode(_component_n_descriptions, {
                            column: 2,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(_component_n_descriptions_item, { label: "ID" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentTag.value.id), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "标签名称" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentTag.value.tagName), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "标签标识" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentTag.value.tagKey), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "标签颜色" }, {
                                default: vue.withCtx(() => [
                                  vue.createVNode(_component_n_space, { size: 8 }, {
                                    default: vue.withCtx(() => [
                                      currentTag.value.color ? (vue.openBlock(), vue.createElementBlock("div", {
                                        key: 0,
                                        style: vue.normalizeStyle({
                                          width: "20px",
                                          height: "20px",
                                          borderRadius: "4px",
                                          backgroundColor: currentTag.value.color,
                                          border: "1px solid #ddd"
                                        })
                                      }, null, 4)) : vue.createCommentVNode("", true),
                                      vue.createElementVNode("span", null, vue.toDisplayString(currentTag.value.color || "-"), 1)
                                    ]),
                                    _: 1
                                  })
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "启用状态" }, {
                                default: vue.withCtx(() => [
                                  vue.createVNode(_component_n_tag, {
                                    type: currentTag.value.enabled ? "success" : "default"
                                  }, {
                                    default: vue.withCtx(() => [
                                      vue.createTextVNode(vue.toDisplayString(currentTag.value.enabled ? "已启用" : "已禁用"), 1)
                                    ]),
                                    _: 1
                                  }, 8, ["type"])
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "使用次数" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentTag.value.usageCount), 1)
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          }),
                          vue.createVNode(_component_n_divider),
                          vue.createVNode(_component_n_descriptions, {
                            column: 1,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(_component_n_descriptions_item, { label: "标签描述" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentTag.value.description || "-"), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "创建时间" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(formatDateTime(currentTag.value.createTime)), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "更新时间" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(formatDateTime(currentTag.value.updateTime)), 1)
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      })
                    ])) : vue.createCommentVNode("", true)
                  ]),
                  _: 1
                }, 8, ["title"])
              ];
            }),
            _: 1
          }, 8, ["show"])
        ]);
      };
    }
  });
  const TagManagement = /* @__PURE__ */ _export_sfc(_sfc_main$6, [["__scopeId", "data-v-cacd4f23"]]);
  const _hoisted_1$5 = { class: "developer-management-page" };
  const _hoisted_2$5 = { class: "page-header-wrapper" };
  const _hoisted_3$5 = { class: "page-content" };
  const _hoisted_4$5 = { class: "table-container" };
  const _hoisted_5$5 = { class: "pagination-container" };
  const _hoisted_6$3 = {
    key: 0,
    class: "developer-detail"
  };
  const _hoisted_7$2 = ["href"];
  const _hoisted_8$1 = { key: 1 };
  const _hoisted_9 = { class: "text-secondary" };
  const _sfc_main$5 = /* @__PURE__ */ vue.defineComponent({
    __name: "DeveloperManagement",
    setup(__props) {
      const Refresh = useIcon("RefreshOutline");
      const Eye = useIcon("EyeOutline");
      const CheckmarkCircleOutline = useIcon("CheckmarkCircleOutline");
      const BanOutline = useIcon("BanOutline");
      const PlayCircleOutline = useIcon("PlayCircleOutline");
      const message = useMessage();
      const dialog = naiveUi.useDialog();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const developers = vue.ref([]);
      const showDetailDrawer = vue.ref(false);
      const currentDeveloper = vue.ref(null);
      const showAdvanced = vue.ref(false);
      const filters = vue.ref({
        keyword: "",
        status: null,
        verified: null
      });
      const pagination = vue.reactive({
        page: 1,
        pageSize: 20,
        itemCount: 0,
        pageSizes: [10, 20, 50, 100]
      });
      const handlePageChange = (page) => {
        pagination.page = page;
        loadDevelopers();
      };
      const handlePageSizeChange = (pageSize) => {
        pagination.pageSize = pageSize;
        pagination.page = 1;
        loadDevelopers();
      };
      const basicFields = [
        {
          key: "keyword",
          label: "关键词",
          type: "input",
          placeholder: "搜索用户名、邮箱",
          span: 12
        },
        {
          key: "status",
          label: "状态",
          type: "select",
          placeholder: "请选择状态",
          options: [
            { label: "全部", value: null },
            { label: "待审核", value: "PENDING" },
            { label: "已激活", value: "ACTIVE" },
            { label: "已暂停", value: "SUSPENDED" }
          ],
          span: 6
        },
        {
          key: "verified",
          label: "验证状态",
          type: "select",
          placeholder: "请选择验证状态",
          options: [
            { label: "全部", value: null },
            { label: "已验证", value: true },
            { label: "未验证", value: false }
          ],
          span: 6
        }
      ];
      const columns = [
        {
          title: "ID",
          key: "id",
          width: 80
        },
        {
          title: "用户名",
          key: "username",
          width: 150
        },
        {
          title: "邮箱",
          key: "email",
          width: 200
        },
        {
          title: "显示名称",
          key: "displayName",
          width: 150,
          render: (row) => row.displayName || "-"
        },
        {
          title: "公司",
          key: "company",
          width: 150,
          render: (row) => row.company || "-"
        },
        {
          title: "状态",
          key: "status",
          width: 100,
          render: (row) => {
            const NTag = vue.resolveComponent("NTag");
            return vue.h(
              NTag,
              { type: getStatusType(row.status) },
              { default: () => getStatusText(row.status) }
            );
          }
        },
        {
          title: "验证",
          key: "verified",
          width: 80,
          render: (row) => {
            const NTag = vue.resolveComponent("NTag");
            return vue.h(
              NTag,
              { type: row.verified ? "success" : "default", size: "small" },
              { default: () => row.verified ? "已验证" : "未验证" }
            );
          }
        },
        {
          title: "插件数",
          key: "pluginCount",
          width: 80
        },
        {
          title: "下载量",
          key: "totalDownloads",
          width: 100
        },
        {
          title: "申请时间",
          key: "applyTime",
          width: 180,
          render: (row) => formatDateTime(row.applyTime)
        },
        {
          title: "操作",
          key: "actions",
          width: 150,
          fixed: "right",
          render: (row) => {
            const NButton = vue.resolveComponent("NButton");
            const NSpace = vue.resolveComponent("NSpace");
            const NIcon = vue.resolveComponent("NIcon");
            const NDropdown = vue.resolveComponent("NDropdown");
            const MoreHorizontal = useIcon("EllipsisHorizontal");
            const moreOptions = [];
            if (row.status === "PENDING") {
              moreOptions.push({
                label: "批准",
                key: "approve",
                icon: () => vue.h(NIcon, { component: CheckmarkCircleOutline })
              });
            }
            if (row.status === "ACTIVE") {
              moreOptions.push({
                label: "暂停",
                key: "suspend",
                icon: () => vue.h(NIcon, { component: BanOutline })
              });
            }
            if (row.status === "SUSPENDED") {
              moreOptions.push({
                label: "激活",
                key: "activate",
                icon: () => vue.h(NIcon, { component: PlayCircleOutline })
              });
            }
            const handleMoreSelect = (key) => {
              switch (key) {
                case "approve":
                  handleApprove(row);
                  break;
                case "suspend":
                  handleSuspend(row);
                  break;
                case "activate":
                  handleActivate(row);
                  break;
              }
            };
            return vue.h(
              NSpace,
              { size: 2, wrap: false },
              {
                default: () => [
                  vue.h(
                    NButton,
                    {
                      size: "small",
                      onClick: () => handleViewDetail(row.id)
                    },
                    {
                      icon: () => vue.h(NIcon, { component: Eye }),
                      default: () => "查看"
                    }
                  ),
                  moreOptions.length > 0 && vue.h(
                    NDropdown,
                    {
                      trigger: "click",
                      options: moreOptions,
                      onSelect: handleMoreSelect
                    },
                    {
                      default: () => vue.h(
                        NButton,
                        {
                          size: "small",
                          quaternary: true
                        },
                        {
                          icon: () => vue.h(NIcon, { component: MoreHorizontal })
                        }
                      )
                    }
                  )
                ]
              }
            );
          }
        }
      ];
      const loadDevelopers = async () => {
        loading.value = true;
        refreshLoading.value = true;
        try {
          const params = {
            page: pagination.page,
            size: pagination.pageSize
          };
          if (filters.value.keyword) {
            params.keyword = filters.value.keyword;
          }
          if (filters.value.status) {
            params.status = filters.value.status;
          }
          if (filters.value.verified !== null) {
            params.verified = filters.value.verified;
          }
          const response = await developerApi.getList(params);
          developers.value = response.items;
          pagination.itemCount = response.total;
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      };
      const handleSearch = () => {
        pagination.page = 1;
        loadDevelopers();
      };
      const handleReset = () => {
        filters.value.keyword = "";
        filters.value.status = null;
        filters.value.verified = null;
        pagination.page = 1;
        loadDevelopers();
      };
      const handleViewDetail = async (id) => {
        const response = await developerApi.getDetail(id);
        currentDeveloper.value = response;
        showDetailDrawer.value = true;
      };
      const handleApprove = (developer) => {
        dialog.warning({
          title: "批准开发者资格",
          content: `确定要批准开发者 "${developer.username}" 的资格申请吗？`,
          positiveText: "批准",
          negativeText: "取消",
          onPositiveClick: async () => {
            await developerApi.approve(developer.id, {
              reviewerId: "admin",
              // TODO: Get from current user
              reviewerName: "管理员",
              // TODO: Get from current user
              comment: "资格审核通过"
            });
            message.success("批准成功");
            loadDevelopers();
          }
        });
      };
      const handleSuspend = (developer) => {
        dialog.warning({
          title: "暂停开发者账户",
          content: `确定要暂停开发者 "${developer.username}" 的账户吗？

暂停后该开发者将无法提交新插件`,
          positiveText: "暂停",
          negativeText: "取消",
          onPositiveClick: async () => {
            await developerApi.suspend(developer.id, {
              operatorId: "admin",
              // TODO: Get from current user
              operatorName: "管理员",
              // TODO: Get from current user
              reason: "违反平台规则"
            });
            message.success("暂停成功");
            loadDevelopers();
          }
        });
      };
      const handleActivate = (developer) => {
        dialog.info({
          title: "激活开发者账户",
          content: `确定要激活开发者 "${developer.username}" 的账户吗？`,
          positiveText: "激活",
          negativeText: "取消",
          onPositiveClick: async () => {
            await developerApi.activate(developer.id, {
              operatorId: "admin",
              // TODO: Get from current user
              operatorName: "管理员",
              // TODO: Get from current user
              comment: "账户已恢复正常"
            });
            message.success("激活成功");
            loadDevelopers();
          }
        });
      };
      const getStatusType = (status) => {
        const typeMap = {
          PENDING: "warning",
          ACTIVE: "success",
          SUSPENDED: "error"
        };
        return typeMap[status] || "default";
      };
      const getStatusText = (status) => {
        const textMap = {
          PENDING: "待审核",
          ACTIVE: "已激活",
          SUSPENDED: "已暂停"
        };
        return textMap[status] || status;
      };
      const getSubmissionStatusType = (status) => {
        const typeMap = {
          PENDING: "warning",
          APPROVED: "success",
          REJECTED: "error"
        };
        return typeMap[status] || "default";
      };
      const getSubmissionStatusText = (status) => {
        const textMap = {
          PENDING: "待审核",
          APPROVED: "已批准",
          REJECTED: "已拒绝"
        };
        return textMap[status] || status;
      };
      const formatDateTime = (dateTime) => {
        if (!dateTime) return "-";
        const date = new Date(dateTime);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit"
        });
      };
      vue.onMounted(() => {
        loadDevelopers();
      });
      return (_ctx, _cache) => {
        const _component_n_icon = vue.resolveComponent("n-icon");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_pagination = vue.resolveComponent("n-pagination");
        const _component_n_descriptions_item = vue.resolveComponent("n-descriptions-item");
        const _component_n_tag = vue.resolveComponent("n-tag");
        const _component_n_descriptions = vue.resolveComponent("n-descriptions");
        const _component_n_divider = vue.resolveComponent("n-divider");
        const _component_n_card = vue.resolveComponent("n-card");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_n_thing = vue.resolveComponent("n-thing");
        const _component_n_list_item = vue.resolveComponent("n-list-item");
        const _component_n_list = vue.resolveComponent("n-list");
        const _component_n_empty = vue.resolveComponent("n-empty");
        const _component_n_drawer_content = vue.resolveComponent("n-drawer-content");
        const _component_n_drawer = vue.resolveComponent("n-drawer");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$5, [
          vue.createElementVNode("div", _hoisted_2$5, [
            vue.createVNode(_component_PageHeader, {
              title: "开发者管理",
              subtitle: "管理开发者账户，审核开发者资格申请"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(_component_n_button, {
                  onClick: loadDevelopers,
                  loading: refreshLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[6] || (_cache[6] = vue.createTextVNode(" 刷新 ", -1))
                  ]),
                  _: 1
                }, 8, ["loading"])
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$5, [
            vue.createVNode(_component_FilterPanel, {
              filters: filters.value,
              "onUpdate:filters": _cache[0] || (_cache[0] = ($event) => filters.value = $event),
              "show-advanced": showAdvanced.value,
              "onUpdate:showAdvanced": _cache[1] || (_cache[1] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced"]),
            vue.createElementVNode("div", _hoisted_4$5, [
              vue.createVNode(_component_n_data_table, {
                columns,
                data: developers.value,
                loading: loading.value,
                pagination: false,
                "row-key": (row) => row.id,
                striped: ""
              }, null, 8, ["data", "loading", "row-key"]),
              vue.createElementVNode("div", _hoisted_5$5, [
                vue.createVNode(_component_n_pagination, {
                  page: pagination.page,
                  "onUpdate:page": [
                    _cache[2] || (_cache[2] = ($event) => pagination.page = $event),
                    handlePageChange
                  ],
                  "page-size": pagination.pageSize,
                  "onUpdate:pageSize": [
                    _cache[3] || (_cache[3] = ($event) => pagination.pageSize = $event),
                    handlePageSizeChange
                  ],
                  "item-count": pagination.itemCount,
                  "page-sizes": pagination.pageSizes,
                  "show-size-picker": "",
                  "show-quick-jumper": ""
                }, {
                  prefix: vue.withCtx(({ itemCount }) => [
                    vue.createTextVNode(" 共 " + vue.toDisplayString(itemCount) + " 条 ", 1)
                  ]),
                  _: 1
                }, 8, ["page", "page-size", "item-count", "page-sizes"])
              ])
            ])
          ]),
          vue.createVNode(_component_n_drawer, {
            show: showDetailDrawer.value,
            "onUpdate:show": _cache[5] || (_cache[5] = ($event) => showDetailDrawer.value = $event),
            width: 800,
            placement: "right"
          }, {
            default: vue.withCtx(() => {
              var _a;
              return [
                vue.createVNode(_component_n_drawer_content, {
                  title: `开发者详情 - ${((_a = currentDeveloper.value) == null ? void 0 : _a.username) || ""}`
                }, {
                  footer: vue.withCtx(() => [
                    vue.createVNode(_component_n_space, { justify: "end" }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_button, {
                          onClick: _cache[4] || (_cache[4] = ($event) => showDetailDrawer.value = false)
                        }, {
                          default: vue.withCtx(() => [..._cache[7] || (_cache[7] = [
                            vue.createTextVNode("关闭", -1)
                          ])]),
                          _: 1
                        })
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    currentDeveloper.value ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_6$3, [
                      vue.createVNode(_component_n_card, {
                        title: "基本信息",
                        bordered: false,
                        class: "detail-section"
                      }, {
                        default: vue.withCtx(() => [
                          vue.createVNode(_component_n_descriptions, {
                            column: 2,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(_component_n_descriptions_item, { label: "用户ID" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.userId), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "用户名" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.username), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "邮箱" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.email), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "显示名称" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.displayName || "-"), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "公司" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.company || "-"), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "网站" }, {
                                default: vue.withCtx(() => [
                                  currentDeveloper.value.website ? (vue.openBlock(), vue.createElementBlock("a", {
                                    key: 0,
                                    href: currentDeveloper.value.website,
                                    target: "_blank"
                                  }, vue.toDisplayString(currentDeveloper.value.website), 9, _hoisted_7$2)) : (vue.openBlock(), vue.createElementBlock("span", _hoisted_8$1, "-"))
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, {
                                label: "状态",
                                span: 2
                              }, {
                                default: vue.withCtx(() => [
                                  vue.createVNode(_component_n_tag, {
                                    type: getStatusType(currentDeveloper.value.status)
                                  }, {
                                    default: vue.withCtx(() => [
                                      vue.createTextVNode(vue.toDisplayString(getStatusText(currentDeveloper.value.status)), 1)
                                    ]),
                                    _: 1
                                  }, 8, ["type"])
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, {
                                label: "验证状态",
                                span: 2
                              }, {
                                default: vue.withCtx(() => [
                                  vue.createVNode(_component_n_tag, {
                                    type: currentDeveloper.value.verified ? "success" : "default"
                                  }, {
                                    default: vue.withCtx(() => [
                                      vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.verified ? "已验证" : "未验证"), 1)
                                    ]),
                                    _: 1
                                  }, 8, ["type"])
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          }),
                          vue.createVNode(_component_n_divider),
                          vue.createVNode(_component_n_descriptions, {
                            column: 1,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(_component_n_descriptions_item, { label: "个人简介" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.bio || "-"), 1)
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }),
                      vue.createVNode(_component_n_card, {
                        title: "统计信息",
                        bordered: false,
                        class: "detail-section"
                      }, {
                        default: vue.withCtx(() => [
                          vue.createVNode(_component_n_descriptions, {
                            column: 2,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(_component_n_descriptions_item, { label: "插件数量" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.pluginCount), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "总下载量" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.totalDownloads), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "申请时间" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(formatDateTime(currentDeveloper.value.applyTime)), 1)
                                ]),
                                _: 1
                              }),
                              vue.createVNode(_component_n_descriptions_item, { label: "审核时间" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.reviewTime ? formatDateTime(currentDeveloper.value.reviewTime) : "-"), 1)
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          }),
                          currentDeveloper.value.reviewComment ? (vue.openBlock(), vue.createElementBlock(vue.Fragment, { key: 0 }, [
                            vue.createVNode(_component_n_divider),
                            vue.createVNode(_component_n_descriptions, {
                              column: 1,
                              "label-placement": "left"
                            }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(_component_n_descriptions_item, { label: "审核意见" }, {
                                  default: vue.withCtx(() => [
                                    vue.createTextVNode(vue.toDisplayString(currentDeveloper.value.reviewComment), 1)
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            })
                          ], 64)) : vue.createCommentVNode("", true)
                        ]),
                        _: 1
                      }),
                      vue.createVNode(_component_n_card, {
                        title: "提交的插件",
                        bordered: false,
                        class: "detail-section"
                      }, {
                        default: vue.withCtx(() => [
                          currentDeveloper.value.plugins && currentDeveloper.value.plugins.length > 0 ? (vue.openBlock(), vue.createBlock(_component_n_list, {
                            key: 0,
                            bordered: ""
                          }, {
                            default: vue.withCtx(() => [
                              (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(currentDeveloper.value.plugins, (plugin) => {
                                return vue.openBlock(), vue.createBlock(_component_n_list_item, {
                                  key: plugin.id
                                }, {
                                  default: vue.withCtx(() => [
                                    vue.createVNode(_component_n_thing, null, {
                                      header: vue.withCtx(() => [
                                        vue.createTextVNode(vue.toDisplayString(plugin.pluginName) + " ", 1),
                                        vue.createVNode(_component_n_tag, {
                                          size: "small",
                                          style: { "margin-left": "8px" }
                                        }, {
                                          default: vue.withCtx(() => [
                                            vue.createTextVNode(vue.toDisplayString(plugin.version), 1)
                                          ]),
                                          _: 2
                                        }, 1024)
                                      ]),
                                      description: vue.withCtx(() => [
                                        vue.createVNode(_component_n_space, { size: 8 }, {
                                          default: vue.withCtx(() => [
                                            vue.createVNode(_component_n_tag, {
                                              size: "small",
                                              type: getSubmissionStatusType(plugin.status)
                                            }, {
                                              default: vue.withCtx(() => [
                                                vue.createTextVNode(vue.toDisplayString(getSubmissionStatusText(plugin.status)), 1)
                                              ]),
                                              _: 2
                                            }, 1032, ["type"]),
                                            vue.createElementVNode("span", _hoisted_9, vue.toDisplayString(formatDateTime(plugin.submitTime)), 1)
                                          ]),
                                          _: 2
                                        }, 1024)
                                      ]),
                                      default: vue.withCtx(() => [
                                        vue.createTextVNode(" " + vue.toDisplayString(plugin.description || "暂无描述"), 1)
                                      ]),
                                      _: 2
                                    }, 1024)
                                  ]),
                                  _: 2
                                }, 1024);
                              }), 128))
                            ]),
                            _: 1
                          })) : (vue.openBlock(), vue.createBlock(_component_n_empty, {
                            key: 1,
                            description: "暂无插件"
                          }))
                        ]),
                        _: 1
                      })
                    ])) : vue.createCommentVNode("", true)
                  ]),
                  _: 1
                }, 8, ["title"])
              ];
            }),
            _: 1
          }, 8, ["show"])
        ]);
      };
    }
  });
  const DeveloperManagement = /* @__PURE__ */ _export_sfc(_sfc_main$5, [["__scopeId", "data-v-70519d49"]]);
  const _hoisted_1$4 = { class: "audit-log-page" };
  const _hoisted_2$4 = { class: "page-header-wrapper" };
  const _hoisted_3$4 = { class: "page-content" };
  const _hoisted_4$4 = { class: "table-container" };
  const _hoisted_5$4 = { class: "pagination-container" };
  const _hoisted_6$2 = {
    key: 0,
    class: "log-detail"
  };
  const _sfc_main$4 = /* @__PURE__ */ vue.defineComponent({
    __name: "AuditLog",
    setup(__props) {
      const Refresh = useIcon("RefreshOutline");
      const Download = useIcon("DownloadOutline");
      const Eye = useIcon("EyeOutline");
      const CheckmarkCircle = useIcon("CheckmarkCircleOutline");
      const CloseCircle = useIcon("CloseCircleOutline");
      const message = useMessage();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const exportLoading = vue.ref(false);
      const tableData = vue.ref([]);
      const showDetailDrawer = vue.ref(false);
      const currentLog = vue.ref(null);
      const showAdvanced = vue.ref(false);
      const filters = vue.ref({
        operationType: null,
        targetType: null,
        result: null,
        keyword: "",
        startTime: null,
        endTime: null
      });
      const pagination = vue.reactive({
        page: 1,
        pageSize: 10,
        itemCount: 0,
        pageSizes: [10, 20, 50, 100]
      });
      const handlePageChange = (page) => {
        pagination.page = page;
        loadData();
      };
      const handlePageSizeChange = (pageSize) => {
        pagination.pageSize = pageSize;
        pagination.page = 1;
        loadData();
      };
      const showExportModal = vue.ref(false);
      const exportForm = vue.reactive({
        format: "CSV",
        timeRange: null
      });
      const basicFields = [
        {
          key: "operationType",
          label: "操作类型",
          type: "select",
          placeholder: "请选择操作类型",
          options: [
            { label: "全部", value: null },
            { label: "批准插件", value: "APPROVE_PLUGIN" },
            { label: "拒绝插件", value: "REJECT_PLUGIN" },
            { label: "下架插件", value: "DELIST_PLUGIN" },
            { label: "重新上架", value: "RELIST_PLUGIN" },
            { label: "删除插件", value: "DELETE_PLUGIN" },
            { label: "批准开发者", value: "APPROVE_DEVELOPER" },
            { label: "暂停开发者", value: "SUSPEND_DEVELOPER" },
            { label: "激活开发者", value: "ACTIVATE_DEVELOPER" },
            { label: "创建规则", value: "CREATE_RULE" },
            { label: "更新规则", value: "UPDATE_RULE" },
            { label: "删除规则", value: "DELETE_RULE" },
            { label: "批量批准", value: "BATCH_APPROVE" },
            { label: "批量拒绝", value: "BATCH_REJECT" },
            { label: "批量下架", value: "BATCH_DELIST" }
          ]
        },
        {
          key: "targetType",
          label: "目标类型",
          type: "select",
          placeholder: "请选择目标类型",
          options: [
            { label: "全部", value: null },
            { label: "插件", value: "PLUGIN" },
            { label: "开发者", value: "DEVELOPER" },
            { label: "规则", value: "RULE" },
            { label: "分类", value: "CATEGORY" },
            { label: "标签", value: "TAG" }
          ]
        },
        {
          key: "result",
          label: "操作结果",
          type: "select",
          placeholder: "请选择操作结果",
          options: [
            { label: "全部", value: null },
            { label: "成功", value: "SUCCESS" },
            { label: "失败", value: "FAILURE" }
          ]
        },
        {
          key: "keyword",
          label: "关键词",
          type: "input",
          placeholder: "搜索操作人、目标名称"
        }
      ];
      const advancedFields = [
        {
          key: "timeRange",
          label: "操作时间",
          type: "date-range",
          placeholder: "选择时间范围",
          span: 2
        }
      ];
      const columns = [
        {
          title: "ID",
          key: "id",
          width: 80
        },
        {
          title: "操作类型",
          key: "operationType",
          width: 120,
          ellipsis: {
            tooltip: true
          },
          render: (row) => {
            const text = getOperationTypeText(row.operationType);
            return vue.h(
              "div",
              {
                style: {
                  maxWidth: "100%",
                  overflow: "hidden",
                  textOverflow: "ellipsis",
                  whiteSpace: "nowrap"
                }
              },
              [
                vue.h(
                  naiveUi.NTag,
                  {
                    type: getOperationTypeColor(row.operationType),
                    size: "small",
                    style: {
                      maxWidth: "100%",
                      display: "inline-block",
                      overflow: "hidden",
                      textOverflow: "ellipsis",
                      whiteSpace: "nowrap"
                    }
                  },
                  { default: () => text }
                )
              ]
            );
          }
        },
        {
          title: "操作名称",
          key: "operationName",
          width: 150,
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "目标类型",
          key: "targetType",
          width: 100,
          render: (row) => row.targetType ? vue.h(
            naiveUi.NTag,
            { size: "small" },
            { default: () => getTargetTypeText(row.targetType) }
          ) : "-"
        },
        {
          title: "目标名称",
          key: "targetName",
          width: 150,
          ellipsis: {
            tooltip: true
          },
          render: (row) => row.targetName || "-"
        },
        {
          title: "操作人",
          key: "operatorName",
          width: 120,
          ellipsis: {
            tooltip: true
          },
          render: (row) => row.operatorName || row.operatorId
        },
        {
          title: "操作IP",
          key: "operatorIp",
          width: 130,
          render: (row) => row.operatorIp || "-"
        },
        {
          title: "结果",
          key: "result",
          width: 80,
          render: (row) => vue.h(
            naiveUi.NTag,
            {
              type: row.result === "SUCCESS" ? "success" : "error",
              size: "small"
            },
            {
              default: () => row.result === "SUCCESS" ? "成功" : "失败",
              icon: () => vue.h(naiveUi.NIcon, {
                component: row.result === "SUCCESS" ? CheckmarkCircle : CloseCircle
              })
            }
          )
        },
        {
          title: "操作时间",
          key: "operationTime",
          width: 180,
          render: (row) => formatDateTime(row.operationTime)
        },
        {
          title: "操作",
          key: "actions",
          width: 100,
          fixed: "right",
          render: (row) => vue.h(
            naiveUi.NButton,
            {
              size: "small",
              onClick: () => handleViewDetail(row)
            },
            {
              default: () => "查看详情",
              icon: () => vue.h(naiveUi.NIcon, { component: Eye })
            }
          )
        }
      ];
      const loadData = async () => {
        loading.value = true;
        refreshLoading.value = true;
        try {
          const params = {
            page: pagination.page,
            size: pagination.pageSize
          };
          if (filters.value.operationType) {
            params.operationType = filters.value.operationType;
          }
          if (filters.value.targetType) {
            params.targetType = filters.value.targetType;
          }
          if (filters.value.result) {
            params.result = filters.value.result;
          }
          if (filters.value.keyword && filters.value.keyword.trim()) {
            params.keyword = filters.value.keyword.trim();
          }
          if (filters.value.startTime) {
            const timestamp = typeof filters.value.startTime === "number" ? filters.value.startTime : new Date(filters.value.startTime).getTime();
            params.startTime = timestamp;
          }
          if (filters.value.endTime) {
            const timestamp = typeof filters.value.endTime === "number" ? filters.value.endTime : new Date(filters.value.endTime).getTime();
            params.endTime = timestamp;
          }
          const response = await auditLogApi.getList(params);
          tableData.value = response.items;
          pagination.itemCount = response.total;
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      };
      const handleSearch = () => {
        pagination.page = 1;
        loadData();
      };
      const handleReset = () => {
        filters.value.operationType = null;
        filters.value.targetType = null;
        filters.value.result = null;
        filters.value.keyword = "";
        filters.value.startTime = null;
        filters.value.endTime = null;
        pagination.page = 1;
        loadData();
      };
      const handleViewDetail = async (log) => {
        const response = await auditLogApi.getDetail(log.id);
        currentLog.value = response;
        showDetailDrawer.value = true;
      };
      const handleExport = () => {
        exportForm.format = "CSV";
        exportForm.timeRange = null;
        showExportModal.value = true;
      };
      const confirmExport = async () => {
        const request = {
          format: exportForm.format,
          operationType: filters.value.operationType || void 0,
          targetType: filters.value.targetType || void 0,
          result: filters.value.result || void 0,
          startTime: exportForm.timeRange ? new Date(exportForm.timeRange[0]).toISOString() : void 0,
          endTime: exportForm.timeRange ? new Date(exportForm.timeRange[1]).toISOString() : void 0
        };
        const response = await auditLogApi.export(request);
        const link = document.createElement("a");
        link.href = response;
        link.download = `audit-logs-${Date.now()}.${exportForm.format.toLowerCase()}`;
        link.click();
        message.success("导出成功");
        showExportModal.value = false;
      };
      const getOperationTypeText = (type) => {
        const textMap = {
          APPROVE_PLUGIN: "批准插件",
          REJECT_PLUGIN: "拒绝插件",
          DELIST_PLUGIN: "下架插件",
          RELIST_PLUGIN: "重新上架",
          DELETE_PLUGIN: "删除插件",
          APPROVE_DEVELOPER: "批准开发者",
          SUSPEND_DEVELOPER: "暂停开发者",
          ACTIVATE_DEVELOPER: "激活开发者",
          CREATE_RULE: "创建规则",
          UPDATE_RULE: "更新规则",
          DELETE_RULE: "删除规则",
          BATCH_APPROVE: "批量批准",
          BATCH_REJECT: "批量拒绝",
          BATCH_DELIST: "批量下架",
          OTHER: "其他"
        };
        return textMap[type] || type;
      };
      const getOperationTypeColor = (type) => {
        if (type.includes("APPROVE") || type.includes("ACTIVATE") || type.includes("RELIST")) {
          return "success";
        }
        if (type.includes("REJECT") || type.includes("DELETE") || type.includes("DELIST") || type.includes("SUSPEND")) {
          return "error";
        }
        if (type.includes("CREATE") || type.includes("UPDATE")) {
          return "info";
        }
        return "default";
      };
      const getTargetTypeText = (type) => {
        const textMap = {
          PLUGIN: "插件",
          DEVELOPER: "开发者",
          RULE: "规则",
          CATEGORY: "分类",
          TAG: "标签",
          OTHER: "其他"
        };
        return textMap[type] || type;
      };
      const formatDateTime = (dateTime) => {
        if (!dateTime) return "-";
        const date = new Date(dateTime);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit",
          second: "2-digit"
        });
      };
      vue.onMounted(() => {
        loadData();
      });
      return (_ctx, _cache) => {
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$4, [
          vue.createElementVNode("div", _hoisted_2$4, [
            vue.createVNode(_component_PageHeader, {
              title: "审计日志",
              subtitle: "查看所有管理操作记录，追踪系统变更"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(vue.unref(naiveUi.NButton), {
                  type: "primary",
                  onClick: handleExport,
                  loading: exportLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(vue.unref(naiveUi.NIcon), null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Download))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[9] || (_cache[9] = vue.createTextVNode(" 导出日志 ", -1))
                  ]),
                  _: 1
                }, 8, ["loading"]),
                vue.createVNode(vue.unref(naiveUi.NButton), {
                  onClick: loadData,
                  loading: refreshLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(vue.unref(naiveUi.NIcon), null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[10] || (_cache[10] = vue.createTextVNode(" 刷新 ", -1))
                  ]),
                  _: 1
                }, 8, ["loading"])
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$4, [
            vue.createVNode(_component_FilterPanel, {
              filters: filters.value,
              "onUpdate:filters": _cache[0] || (_cache[0] = ($event) => filters.value = $event),
              "show-advanced": showAdvanced.value,
              "onUpdate:showAdvanced": _cache[1] || (_cache[1] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields,
              "advanced-fields": advancedFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced"]),
            vue.createElementVNode("div", _hoisted_4$4, [
              vue.createVNode(vue.unref(naiveUi.NDataTable), {
                columns,
                data: tableData.value,
                loading: loading.value,
                pagination: false,
                "row-key": (row) => row.id,
                striped: ""
              }, null, 8, ["data", "loading", "row-key"]),
              vue.createElementVNode("div", _hoisted_5$4, [
                vue.createVNode(vue.unref(naiveUi.NPagination), {
                  page: pagination.page,
                  "onUpdate:page": [
                    _cache[2] || (_cache[2] = ($event) => pagination.page = $event),
                    handlePageChange
                  ],
                  "page-size": pagination.pageSize,
                  "onUpdate:pageSize": [
                    _cache[3] || (_cache[3] = ($event) => pagination.pageSize = $event),
                    handlePageSizeChange
                  ],
                  "item-count": pagination.itemCount,
                  "page-sizes": pagination.pageSizes,
                  "show-size-picker": "",
                  "show-quick-jumper": ""
                }, {
                  prefix: vue.withCtx(({ itemCount }) => [
                    vue.createTextVNode(" 共 " + vue.toDisplayString(itemCount) + " 条 ", 1)
                  ]),
                  _: 1
                }, 8, ["page", "page-size", "item-count", "page-sizes"])
              ])
            ])
          ]),
          vue.createVNode(vue.unref(naiveUi.NDrawer), {
            show: showDetailDrawer.value,
            "onUpdate:show": _cache[5] || (_cache[5] = ($event) => showDetailDrawer.value = $event),
            width: 720,
            placement: "right"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(vue.unref(naiveUi.NDrawerContent), {
                title: "审计日志详情",
                closable: ""
              }, {
                footer: vue.withCtx(() => [
                  vue.createVNode(vue.unref(naiveUi.NSpace), { justify: "end" }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(vue.unref(naiveUi.NButton), {
                        onClick: _cache[4] || (_cache[4] = ($event) => showDetailDrawer.value = false)
                      }, {
                        default: vue.withCtx(() => [..._cache[11] || (_cache[11] = [
                          vue.createTextVNode("关闭", -1)
                        ])]),
                        _: 1
                      })
                    ]),
                    _: 1
                  })
                ]),
                default: vue.withCtx(() => [
                  currentLog.value ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_6$2, [
                    vue.createVNode(vue.unref(naiveUi.NCard), {
                      title: "基本信息",
                      bordered: false,
                      class: "detail-section"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(vue.unref(naiveUi.NDescriptions), {
                          column: 2,
                          "label-placement": "left"
                        }, {
                          default: vue.withCtx(() => [
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), { label: "日志ID" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentLog.value.id), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), { label: "操作时间" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(formatDateTime(currentLog.value.operationTime)), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), { label: "操作类型" }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(vue.unref(naiveUi.NTag), {
                                  type: getOperationTypeColor(currentLog.value.operationType),
                                  size: "small"
                                }, {
                                  default: vue.withCtx(() => [
                                    vue.createTextVNode(vue.toDisplayString(getOperationTypeText(currentLog.value.operationType)), 1)
                                  ]),
                                  _: 1
                                }, 8, ["type"])
                              ]),
                              _: 1
                            }),
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), { label: "操作名称" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentLog.value.operationName), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), {
                              label: "操作结果",
                              span: 2
                            }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(vue.unref(naiveUi.NTag), {
                                  type: currentLog.value.result === "SUCCESS" ? "success" : "error",
                                  size: "small"
                                }, {
                                  default: vue.withCtx(() => [
                                    vue.createTextVNode(vue.toDisplayString(currentLog.value.result === "SUCCESS" ? "成功" : "失败"), 1)
                                  ]),
                                  _: 1
                                }, 8, ["type"])
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        currentLog.value.operationDesc ? (vue.openBlock(), vue.createElementBlock(vue.Fragment, { key: 0 }, [
                          vue.createVNode(vue.unref(naiveUi.NDivider)),
                          vue.createVNode(vue.unref(naiveUi.NDescriptions), {
                            column: 1,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), { label: "操作描述" }, {
                                default: vue.withCtx(() => [
                                  vue.createTextVNode(vue.toDisplayString(currentLog.value.operationDesc), 1)
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          })
                        ], 64)) : vue.createCommentVNode("", true)
                      ]),
                      _: 1
                    }),
                    vue.createVNode(vue.unref(naiveUi.NCard), {
                      title: "操作人信息",
                      bordered: false,
                      class: "detail-section"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(vue.unref(naiveUi.NDescriptions), {
                          column: 2,
                          "label-placement": "left"
                        }, {
                          default: vue.withCtx(() => [
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), { label: "操作人ID" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentLog.value.operatorId), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), { label: "操作人名称" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentLog.value.operatorName || "-"), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), {
                              label: "操作IP",
                              span: 2
                            }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentLog.value.operatorIp || "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    }),
                    currentLog.value.targetType ? (vue.openBlock(), vue.createBlock(vue.unref(naiveUi.NCard), {
                      key: 0,
                      title: "目标对象",
                      bordered: false,
                      class: "detail-section"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(vue.unref(naiveUi.NDescriptions), {
                          column: 2,
                          "label-placement": "left"
                        }, {
                          default: vue.withCtx(() => [
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), { label: "目标类型" }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(vue.unref(naiveUi.NTag), { size: "small" }, {
                                  default: vue.withCtx(() => [
                                    vue.createTextVNode(vue.toDisplayString(getTargetTypeText(currentLog.value.targetType)), 1)
                                  ]),
                                  _: 1
                                })
                              ]),
                              _: 1
                            }),
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), { label: "目标ID" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentLog.value.targetId || "-"), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(vue.unref(naiveUi.NDescriptionsItem), {
                              label: "目标名称",
                              span: 2
                            }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentLog.value.targetName || "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    })) : vue.createCommentVNode("", true),
                    currentLog.value.result === "FAILURE" && currentLog.value.errorMessage ? (vue.openBlock(), vue.createBlock(vue.unref(naiveUi.NCard), {
                      key: 1,
                      title: "错误信息",
                      bordered: false,
                      class: "detail-section"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(vue.unref(naiveUi.NAlert), {
                          type: "error",
                          "show-icon": false
                        }, {
                          default: vue.withCtx(() => [
                            vue.createTextVNode(vue.toDisplayString(currentLog.value.errorMessage), 1)
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    })) : vue.createCommentVNode("", true),
                    currentLog.value.beforeData || currentLog.value.afterData ? (vue.openBlock(), vue.createBlock(vue.unref(naiveUi.NCard), {
                      key: 2,
                      title: "变更内容",
                      bordered: false,
                      class: "detail-section"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(vue.unref(naiveUi.NTabs), { type: "line" }, {
                          default: vue.withCtx(() => [
                            currentLog.value.beforeData ? (vue.openBlock(), vue.createBlock(vue.unref(naiveUi.NTabPane), {
                              key: 0,
                              name: "before",
                              tab: "变更前"
                            }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(vue.unref(naiveUi.NCode), {
                                  code: JSON.stringify(currentLog.value.beforeData, null, 2),
                                  language: "json"
                                }, null, 8, ["code"])
                              ]),
                              _: 1
                            })) : vue.createCommentVNode("", true),
                            currentLog.value.afterData ? (vue.openBlock(), vue.createBlock(vue.unref(naiveUi.NTabPane), {
                              key: 1,
                              name: "after",
                              tab: "变更后"
                            }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(vue.unref(naiveUi.NCode), {
                                  code: JSON.stringify(currentLog.value.afterData, null, 2),
                                  language: "json"
                                }, null, 8, ["code"])
                              ]),
                              _: 1
                            })) : vue.createCommentVNode("", true)
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    })) : vue.createCommentVNode("", true)
                  ])) : vue.createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["show"]),
          vue.createVNode(vue.unref(naiveUi.NModal), {
            show: showExportModal.value,
            "onUpdate:show": _cache[8] || (_cache[8] = ($event) => showExportModal.value = $event),
            preset: "dialog",
            title: "导出审计日志",
            "positive-text": "确认导出",
            "negative-text": "取消",
            onPositiveClick: confirmExport
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(vue.unref(naiveUi.NForm), { model: exportForm }, {
                default: vue.withCtx(() => [
                  vue.createVNode(vue.unref(naiveUi.NFormItem), { label: "导出格式" }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(vue.unref(naiveUi.NRadioGroup), {
                        value: exportForm.format,
                        "onUpdate:value": _cache[6] || (_cache[6] = ($event) => exportForm.format = $event)
                      }, {
                        default: vue.withCtx(() => [
                          vue.createVNode(vue.unref(naiveUi.NSpace), null, {
                            default: vue.withCtx(() => [
                              vue.createVNode(vue.unref(naiveUi.NRadio), { value: "CSV" }, {
                                default: vue.withCtx(() => [..._cache[12] || (_cache[12] = [
                                  vue.createTextVNode("CSV", -1)
                                ])]),
                                _: 1
                              }),
                              vue.createVNode(vue.unref(naiveUi.NRadio), { value: "EXCEL" }, {
                                default: vue.withCtx(() => [..._cache[13] || (_cache[13] = [
                                  vue.createTextVNode("Excel", -1)
                                ])]),
                                _: 1
                              }),
                              vue.createVNode(vue.unref(naiveUi.NRadio), { value: "JSON" }, {
                                default: vue.withCtx(() => [..._cache[14] || (_cache[14] = [
                                  vue.createTextVNode("JSON", -1)
                                ])]),
                                _: 1
                              })
                            ]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(vue.unref(naiveUi.NFormItem), { label: "时间范围" }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(vue.unref(naiveUi.NDatePicker), {
                        value: exportForm.timeRange,
                        "onUpdate:value": _cache[7] || (_cache[7] = ($event) => exportForm.timeRange = $event),
                        type: "datetimerange",
                        clearable: "",
                        style: { "width": "100%" }
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(vue.unref(naiveUi.NAlert), {
                    type: "info",
                    "show-icon": false,
                    style: { "margin-top": "16px" }
                  }, {
                    default: vue.withCtx(() => [..._cache[15] || (_cache[15] = [
                      vue.createTextVNode(" 将导出当前筛选条件下的所有日志记录 ", -1)
                    ])]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show"])
        ]);
      };
    }
  });
  const AuditLog = /* @__PURE__ */ _export_sfc(_sfc_main$4, [["__scopeId", "data-v-687b2df6"]]);
  const _hoisted_1$3 = { class: "review-rules-page" };
  const _hoisted_2$3 = { class: "page-header-wrapper" };
  const _hoisted_3$3 = { class: "page-content" };
  const _hoisted_4$3 = { class: "table-container" };
  const _hoisted_5$3 = { class: "pagination-container" };
  const _hoisted_6$1 = { class: "conditions-container" };
  const _hoisted_7$1 = { class: "actions-container" };
  const _sfc_main$3 = /* @__PURE__ */ vue.defineComponent({
    __name: "ReviewRules",
    setup(__props) {
      const Add = useIcon("AddOutline");
      const Refresh = useIcon("RefreshOutline");
      const TrashOutline = useIcon("TrashOutline");
      const CreateOutline = useIcon("CreateOutline");
      const BanOutline = useIcon("BanOutline");
      const CheckmarkCircleOutline = useIcon("CheckmarkCircleOutline");
      const message = useMessage();
      const dialog = naiveUi.useDialog();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const submitting = vue.ref(false);
      const rules = vue.ref([]);
      const showRuleModal = vue.ref(false);
      const isEditing = vue.ref(false);
      const currentRuleId = vue.ref(null);
      const formRef = vue.ref(null);
      const showAdvanced = vue.ref(false);
      const filters = vue.ref({
        keyword: "",
        ruleType: null,
        enabled: null
      });
      const pagination = vue.reactive({
        page: 1,
        pageSize: 20,
        itemCount: 0,
        pageSizes: [10, 20, 50, 100]
      });
      const handlePageChange = (page) => {
        pagination.page = page;
        loadRules();
      };
      const handlePageSizeChange = (pageSize) => {
        pagination.pageSize = pageSize;
        pagination.page = 1;
        loadRules();
      };
      const ruleForm = vue.reactive({
        ruleName: "",
        description: "",
        ruleType: "AUTO_APPROVE",
        priority: 0,
        enabled: true,
        conditions: {
          logic: "AND",
          conditions: []
        },
        actions: []
      });
      const formRules = {
        ruleName: [
          { required: true, message: "请输入规则名称", trigger: "blur" },
          { min: 2, max: 128, message: "规则名称长度为 2-128 个字符", trigger: "blur" }
        ],
        ruleType: [
          { required: true, message: "请选择规则类型", trigger: "change" }
        ],
        priority: [
          { required: true, type: "number", message: "请输入优先级", trigger: "blur" }
        ],
        "conditions.logic": [
          { required: true, message: "请选择条件逻辑", trigger: "change" }
        ]
      };
      const basicFields = [
        {
          key: "ruleType",
          label: "规则类型",
          type: "select",
          placeholder: "请选择规则类型",
          options: [
            { label: "全部", value: null },
            { label: "自动批准", value: "AUTO_APPROVE" },
            { label: "自动拒绝", value: "AUTO_REJECT" },
            { label: "标记", value: "FLAG" }
          ]
        },
        {
          key: "enabled",
          label: "启用状态",
          type: "select",
          placeholder: "请选择启用状态",
          options: [
            { label: "全部", value: null },
            { label: "已启用", value: true },
            { label: "已禁用", value: false }
          ]
        },
        {
          key: "keyword",
          label: "关键词",
          type: "input",
          placeholder: "搜索规则名称、描述"
        }
      ];
      const ruleTypeOptions = [
        { label: "自动批准", value: "AUTO_APPROVE" },
        { label: "自动拒绝", value: "AUTO_REJECT" },
        { label: "标记", value: "FLAG" }
      ];
      const fieldOptions = [
        { label: "插件类型", value: "pluginType" },
        { label: "开发者ID", value: "developerId" },
        { label: "插件名称", value: "pluginName" },
        { label: "文件大小", value: "fileSize" },
        { label: "标签", value: "tags" },
        { label: "分类", value: "category" }
      ];
      const operatorOptions = [
        { label: "等于", value: "equals" },
        { label: "包含", value: "contains" },
        { label: "大于", value: "greater_than" },
        { label: "小于", value: "less_than" },
        { label: "在列表中", value: "in" },
        { label: "不在列表中", value: "not_in" }
      ];
      const actionTypeOptions = [
        { label: "批准", value: "approve" },
        { label: "拒绝", value: "reject" },
        { label: "标记", value: "flag" },
        { label: "通知", value: "notify" }
      ];
      const columns = [
        {
          title: "ID",
          key: "id",
          width: 80
        },
        {
          title: "规则名称",
          key: "ruleName",
          width: 200
        },
        {
          title: "描述",
          key: "description",
          width: 250,
          ellipsis: {
            tooltip: true
          },
          render: (row) => row.description || "-"
        },
        {
          title: "规则类型",
          key: "ruleType",
          width: 120,
          render: (row) => vue.h(
            naiveUi.NTag,
            { type: getRuleTypeTagType(row.ruleType) },
            { default: () => getRuleTypeText(row.ruleType) }
          )
        },
        {
          title: "优先级",
          key: "priority",
          width: 100
        },
        {
          title: "状态",
          key: "enabled",
          width: 100,
          render: (row) => vue.h(
            naiveUi.NTag,
            { type: row.enabled ? "success" : "default" },
            { default: () => row.enabled ? "已启用" : "已禁用" }
          )
        },
        {
          title: "匹配次数",
          key: "matchCount",
          width: 100
        },
        {
          title: "最后匹配",
          key: "lastMatchTime",
          width: 180,
          render: (row) => row.lastMatchTime ? formatDateTime(row.lastMatchTime) : "-"
        },
        {
          title: "创建时间",
          key: "createTime",
          width: 180,
          render: (row) => formatDateTime(row.createTime)
        },
        {
          title: "操作",
          key: "actions",
          width: 180,
          fixed: "right",
          render: (row) => {
            const NButton = vue.resolveComponent("NButton");
            const NSpace = vue.resolveComponent("NSpace");
            const NIcon = vue.resolveComponent("NIcon");
            const NDropdown = vue.resolveComponent("NDropdown");
            const MoreHorizontal = useIcon("EllipsisHorizontal");
            const moreOptions = [
              {
                label: row.enabled ? "禁用" : "启用",
                key: row.enabled ? "disable" : "enable",
                icon: () => vue.h(NIcon, { component: row.enabled ? BanOutline : CheckmarkCircleOutline })
              },
              {
                label: "删除",
                key: "delete",
                icon: () => vue.h(NIcon, { component: TrashOutline })
              }
            ];
            const handleMoreSelect = (key) => {
              switch (key) {
                case "enable":
                  handleEnable(row);
                  break;
                case "disable":
                  handleDisable(row);
                  break;
                case "delete":
                  handleDelete(row);
                  break;
              }
            };
            return vue.h(
              NSpace,
              { size: 4, wrap: false },
              {
                default: () => [
                  // 编辑按钮 - 始终显示
                  vue.h(
                    NButton,
                    {
                      size: "small",
                      type: "primary",
                      text: true,
                      onClick: () => handleEdit(row)
                    },
                    {
                      icon: () => vue.h(NIcon, { component: CreateOutline }),
                      default: () => "编辑"
                    }
                  ),
                  // 更多操作下拉菜单
                  vue.h(
                    NDropdown,
                    {
                      trigger: "click",
                      options: moreOptions,
                      onSelect: handleMoreSelect
                    },
                    {
                      default: () => vue.h(
                        NButton,
                        {
                          size: "small",
                          quaternary: true
                        },
                        {
                          icon: () => vue.h(NIcon, { component: MoreHorizontal })
                        }
                      )
                    }
                  )
                ]
              }
            );
          }
        }
      ];
      const loadRules = async () => {
        loading.value = true;
        refreshLoading.value = true;
        try {
          const params = {
            page: pagination.page,
            size: pagination.pageSize,
            keyword: filters.value.keyword || void 0,
            ruleType: filters.value.ruleType || void 0,
            enabled: filters.value.enabled !== null ? filters.value.enabled : void 0
          };
          const response = await reviewRuleApi.getList(params);
          if (Array.isArray(response)) {
            rules.value = response;
            pagination.itemCount = response.length;
          } else {
            rules.value = response.items || [];
            pagination.itemCount = response.total || 0;
          }
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      };
      const handleSearch = () => {
        pagination.page = 1;
        loadRules();
      };
      const handleReset = () => {
        filters.value.keyword = "";
        filters.value.ruleType = null;
        filters.value.enabled = null;
        pagination.page = 1;
        loadRules();
      };
      const handleCreate = () => {
        isEditing.value = false;
        currentRuleId.value = null;
        resetForm();
        showRuleModal.value = true;
      };
      const handleEdit = (rule) => {
        isEditing.value = true;
        currentRuleId.value = rule.id;
        ruleForm.ruleName = rule.ruleName;
        ruleForm.description = rule.description || "";
        ruleForm.ruleType = rule.ruleType;
        ruleForm.priority = rule.priority;
        ruleForm.enabled = rule.enabled;
        ruleForm.conditions = JSON.parse(JSON.stringify(rule.conditions));
        ruleForm.actions = JSON.parse(JSON.stringify(rule.actions));
        showRuleModal.value = true;
      };
      const handleSubmit = async () => {
        if (!formRef.value) return;
        try {
          await formRef.value.validate();
          if (ruleForm.conditions.conditions.length === 0) {
            message.error("请至少添加一个规则条件");
            return;
          }
          if (ruleForm.actions.length === 0) {
            message.error("请至少添加一个规则动作");
            return;
          }
          submitting.value = true;
          const requestData = {
            ruleName: ruleForm.ruleName,
            description: ruleForm.description,
            ruleType: ruleForm.ruleType,
            priority: ruleForm.priority,
            enabled: ruleForm.enabled,
            conditions: ruleForm.conditions,
            actions: ruleForm.actions
          };
          let response;
          if (isEditing.value && currentRuleId.value) {
            response = await reviewRuleApi.update(currentRuleId.value, requestData);
          } else {
            response = await reviewRuleApi.create(requestData);
          }
          message.success(isEditing.value ? "更新成功" : "创建成功");
          showRuleModal.value = false;
          loadRules();
        } catch (error) {
          if (error.errors) {
            return;
          }
          console.error("提交失败:", error);
        } finally {
          submitting.value = false;
        }
      };
      const handleEnable = async (rule) => {
        await reviewRuleApi.enable(rule.id);
        message.success("启用成功");
        loadRules();
      };
      const handleDisable = async (rule) => {
        await reviewRuleApi.disable(rule.id);
        message.success("禁用成功");
        loadRules();
      };
      const handleDelete = (rule) => {
        dialog.warning({
          title: "删除规则",
          content: `确定要删除规则 "${rule.ruleName}" 吗？`,
          positiveText: "删除",
          negativeText: "取消",
          onPositiveClick: async () => {
            await reviewRuleApi.delete(rule.id);
            message.success("删除成功");
            loadRules();
          }
        });
      };
      const addCondition = () => {
        ruleForm.conditions.conditions.push({
          field: "pluginType",
          operator: "equals",
          value: ""
        });
      };
      const removeCondition = (index2) => {
        ruleForm.conditions.conditions.splice(index2, 1);
      };
      const addAction = () => {
        ruleForm.actions.push({
          type: "approve",
          params: {}
        });
      };
      const removeAction = (index2) => {
        ruleForm.actions.splice(index2, 1);
      };
      const resetForm = () => {
        ruleForm.ruleName = "";
        ruleForm.description = "";
        ruleForm.ruleType = "AUTO_APPROVE";
        ruleForm.priority = 0;
        ruleForm.enabled = true;
        ruleForm.conditions = {
          logic: "AND",
          conditions: []
        };
        ruleForm.actions = [];
      };
      const getRuleTypeTagType = (ruleType) => {
        const typeMap = {
          AUTO_APPROVE: "success",
          AUTO_REJECT: "error",
          FLAG: "warning"
        };
        return typeMap[ruleType] || "default";
      };
      const getRuleTypeText = (ruleType) => {
        const textMap = {
          AUTO_APPROVE: "自动批准",
          AUTO_REJECT: "自动拒绝",
          FLAG: "标记"
        };
        return textMap[ruleType] || ruleType;
      };
      const formatDateTime = (dateTime) => {
        if (!dateTime) return "-";
        const date = new Date(dateTime);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit"
        });
      };
      vue.onMounted(() => {
        loadRules();
      });
      return (_ctx, _cache) => {
        const _component_n_icon = vue.resolveComponent("n-icon");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_pagination = vue.resolveComponent("n-pagination");
        const _component_n_input = vue.resolveComponent("n-input");
        const _component_n_form_item = vue.resolveComponent("n-form-item");
        const _component_n_select = vue.resolveComponent("n-select");
        const _component_n_input_number = vue.resolveComponent("n-input-number");
        const _component_n_switch = vue.resolveComponent("n-switch");
        const _component_n_divider = vue.resolveComponent("n-divider");
        const _component_n_radio = vue.resolveComponent("n-radio");
        const _component_n_radio_group = vue.resolveComponent("n-radio-group");
        const _component_n_form = vue.resolveComponent("n-form");
        const _component_n_modal = vue.resolveComponent("n-modal");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$3, [
          vue.createElementVNode("div", _hoisted_2$3, [
            vue.createVNode(_component_PageHeader, {
              title: "审核规则配置",
              subtitle: "配置自动化审核规则，提高审核效率"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(_component_n_space, null, {
                  default: vue.withCtx(() => [
                    vue.createVNode(_component_n_button, {
                      type: "primary",
                      onClick: handleCreate
                    }, {
                      icon: vue.withCtx(() => [
                        vue.createVNode(_component_n_icon, null, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Add))))
                          ]),
                          _: 1
                        })
                      ]),
                      default: vue.withCtx(() => [
                        _cache[12] || (_cache[12] = vue.createTextVNode(" 创建规则 ", -1))
                      ]),
                      _: 1
                    }),
                    vue.createVNode(_component_n_button, {
                      onClick: loadRules,
                      loading: refreshLoading.value
                    }, {
                      icon: vue.withCtx(() => [
                        vue.createVNode(_component_n_icon, null, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                          ]),
                          _: 1
                        })
                      ]),
                      default: vue.withCtx(() => [
                        _cache[13] || (_cache[13] = vue.createTextVNode(" 刷新 ", -1))
                      ]),
                      _: 1
                    }, 8, ["loading"])
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$3, [
            vue.createVNode(_component_FilterPanel, {
              filters: filters.value,
              "onUpdate:filters": _cache[0] || (_cache[0] = ($event) => filters.value = $event),
              "show-advanced": showAdvanced.value,
              "onUpdate:showAdvanced": _cache[1] || (_cache[1] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced"]),
            vue.createElementVNode("div", _hoisted_4$3, [
              vue.createVNode(_component_n_data_table, {
                columns,
                data: rules.value,
                loading: loading.value,
                pagination: false,
                "row-key": (row) => row.id,
                striped: ""
              }, null, 8, ["data", "loading", "row-key"]),
              vue.createElementVNode("div", _hoisted_5$3, [
                vue.createVNode(_component_n_pagination, {
                  page: pagination.page,
                  "onUpdate:page": [
                    _cache[2] || (_cache[2] = ($event) => pagination.page = $event),
                    handlePageChange
                  ],
                  "page-size": pagination.pageSize,
                  "onUpdate:pageSize": [
                    _cache[3] || (_cache[3] = ($event) => pagination.pageSize = $event),
                    handlePageSizeChange
                  ],
                  "item-count": pagination.itemCount,
                  "page-sizes": pagination.pageSizes,
                  "show-size-picker": "",
                  "show-quick-jumper": ""
                }, {
                  prefix: vue.withCtx(({ itemCount }) => [
                    vue.createTextVNode(" 共 " + vue.toDisplayString(itemCount) + " 条 ", 1)
                  ]),
                  _: 1
                }, 8, ["page", "page-size", "item-count", "page-sizes"])
              ])
            ])
          ]),
          vue.createVNode(_component_n_modal, {
            show: showRuleModal.value,
            "onUpdate:show": _cache[11] || (_cache[11] = ($event) => showRuleModal.value = $event),
            "mask-closable": false,
            preset: "card",
            title: isEditing.value ? "编辑规则" : "创建规则",
            style: { "width": "800px" }
          }, {
            footer: vue.withCtx(() => [
              vue.createVNode(_component_n_space, { justify: "end" }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_button, {
                    onClick: _cache[10] || (_cache[10] = ($event) => showRuleModal.value = false)
                  }, {
                    default: vue.withCtx(() => [..._cache[20] || (_cache[20] = [
                      vue.createTextVNode("取消", -1)
                    ])]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_button, {
                    type: "primary",
                    onClick: handleSubmit,
                    loading: submitting.value
                  }, {
                    default: vue.withCtx(() => [
                      vue.createTextVNode(vue.toDisplayString(isEditing.value ? "更新" : "创建"), 1)
                    ]),
                    _: 1
                  }, 8, ["loading"])
                ]),
                _: 1
              })
            ]),
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "formRef",
                ref: formRef,
                model: ruleForm,
                rules: formRules,
                "label-placement": "left",
                "label-width": "120px"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "规则名称",
                    path: "ruleName"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: ruleForm.ruleName,
                        "onUpdate:value": _cache[4] || (_cache[4] = ($event) => ruleForm.ruleName = $event),
                        placeholder: "请输入规则名称",
                        maxlength: "128",
                        "show-count": ""
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "规则描述",
                    path: "description"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: ruleForm.description,
                        "onUpdate:value": _cache[5] || (_cache[5] = ($event) => ruleForm.description = $event),
                        type: "textarea",
                        placeholder: "请输入规则描述",
                        rows: 3,
                        maxlength: "500",
                        "show-count": ""
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "规则类型",
                    path: "ruleType"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_select, {
                        value: ruleForm.ruleType,
                        "onUpdate:value": _cache[6] || (_cache[6] = ($event) => ruleForm.ruleType = $event),
                        options: ruleTypeOptions,
                        placeholder: "请选择规则类型"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "优先级",
                    path: "priority"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input_number, {
                        value: ruleForm.priority,
                        "onUpdate:value": _cache[7] || (_cache[7] = ($event) => ruleForm.priority = $event),
                        min: 0,
                        max: 100,
                        placeholder: "数字越大优先级越高",
                        style: { "width": "100%" }
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "启用状态",
                    path: "enabled"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_switch, {
                        value: ruleForm.enabled,
                        "onUpdate:value": _cache[8] || (_cache[8] = ($event) => ruleForm.enabled = $event)
                      }, {
                        checked: vue.withCtx(() => [..._cache[14] || (_cache[14] = [
                          vue.createTextVNode("启用", -1)
                        ])]),
                        unchecked: vue.withCtx(() => [..._cache[15] || (_cache[15] = [
                          vue.createTextVNode("禁用", -1)
                        ])]),
                        _: 1
                      }, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_divider),
                  vue.createVNode(_component_n_form_item, {
                    label: "条件逻辑",
                    path: "conditions.logic"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_radio_group, {
                        value: ruleForm.conditions.logic,
                        "onUpdate:value": _cache[9] || (_cache[9] = ($event) => ruleForm.conditions.logic = $event)
                      }, {
                        default: vue.withCtx(() => [
                          vue.createVNode(_component_n_radio, { value: "AND" }, {
                            default: vue.withCtx(() => [..._cache[16] || (_cache[16] = [
                              vue.createTextVNode("所有条件都满足（AND）", -1)
                            ])]),
                            _: 1
                          }),
                          vue.createVNode(_component_n_radio, { value: "OR" }, {
                            default: vue.withCtx(() => [..._cache[17] || (_cache[17] = [
                              vue.createTextVNode("任一条件满足（OR）", -1)
                            ])]),
                            _: 1
                          })
                        ]),
                        _: 1
                      }, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "规则条件",
                    path: "conditions.conditions"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createElementVNode("div", _hoisted_6$1, [
                        (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(ruleForm.conditions.conditions, (condition, index2) => {
                          return vue.openBlock(), vue.createElementBlock("div", {
                            key: index2,
                            class: "condition-item"
                          }, [
                            vue.createVNode(_component_n_space, {
                              size: 8,
                              align: "center"
                            }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(_component_n_select, {
                                  value: condition.field,
                                  "onUpdate:value": ($event) => condition.field = $event,
                                  options: fieldOptions,
                                  placeholder: "字段",
                                  style: { "width": "150px" }
                                }, null, 8, ["value", "onUpdate:value"]),
                                vue.createVNode(_component_n_select, {
                                  value: condition.operator,
                                  "onUpdate:value": ($event) => condition.operator = $event,
                                  options: operatorOptions,
                                  placeholder: "操作符",
                                  style: { "width": "120px" }
                                }, null, 8, ["value", "onUpdate:value"]),
                                vue.createVNode(_component_n_input, {
                                  value: condition.value,
                                  "onUpdate:value": ($event) => condition.value = $event,
                                  placeholder: "值",
                                  style: { "width": "200px" }
                                }, null, 8, ["value", "onUpdate:value"]),
                                vue.createVNode(_component_n_button, {
                                  text: "",
                                  type: "error",
                                  onClick: ($event) => removeCondition(index2)
                                }, {
                                  icon: vue.withCtx(() => [
                                    vue.createVNode(_component_n_icon, null, {
                                      default: vue.withCtx(() => [
                                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(TrashOutline))))
                                      ]),
                                      _: 1
                                    })
                                  ]),
                                  _: 1
                                }, 8, ["onClick"])
                              ]),
                              _: 2
                            }, 1024)
                          ]);
                        }), 128)),
                        vue.createVNode(_component_n_button, {
                          dashed: "",
                          block: "",
                          onClick: addCondition,
                          style: { "margin-top": "8px" }
                        }, {
                          icon: vue.withCtx(() => [
                            vue.createVNode(_component_n_icon, null, {
                              default: vue.withCtx(() => [
                                (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Add))))
                              ]),
                              _: 1
                            })
                          ]),
                          default: vue.withCtx(() => [
                            _cache[18] || (_cache[18] = vue.createTextVNode(" 添加条件 ", -1))
                          ]),
                          _: 1
                        })
                      ])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_divider),
                  vue.createVNode(_component_n_form_item, {
                    label: "规则动作",
                    path: "actions"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createElementVNode("div", _hoisted_7$1, [
                        (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(ruleForm.actions, (action, index2) => {
                          return vue.openBlock(), vue.createElementBlock("div", {
                            key: index2,
                            class: "action-item"
                          }, [
                            vue.createVNode(_component_n_space, {
                              size: 8,
                              align: "center"
                            }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(_component_n_select, {
                                  value: action.type,
                                  "onUpdate:value": ($event) => action.type = $event,
                                  options: actionTypeOptions,
                                  placeholder: "动作类型",
                                  style: { "width": "150px" }
                                }, null, 8, ["value", "onUpdate:value"]),
                                action.type === "notify" && action.params ? (vue.openBlock(), vue.createBlock(_component_n_input, {
                                  key: 0,
                                  value: action.params.message,
                                  "onUpdate:value": ($event) => action.params.message = $event,
                                  placeholder: "通知消息",
                                  style: { "width": "300px" }
                                }, null, 8, ["value", "onUpdate:value"])) : vue.createCommentVNode("", true),
                                action.type === "reject" && action.params ? (vue.openBlock(), vue.createBlock(_component_n_input, {
                                  key: 1,
                                  value: action.params.reason,
                                  "onUpdate:value": ($event) => action.params.reason = $event,
                                  placeholder: "拒绝原因",
                                  style: { "width": "300px" }
                                }, null, 8, ["value", "onUpdate:value"])) : vue.createCommentVNode("", true),
                                vue.createVNode(_component_n_button, {
                                  text: "",
                                  type: "error",
                                  onClick: ($event) => removeAction(index2)
                                }, {
                                  icon: vue.withCtx(() => [
                                    vue.createVNode(_component_n_icon, null, {
                                      default: vue.withCtx(() => [
                                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(TrashOutline))))
                                      ]),
                                      _: 1
                                    })
                                  ]),
                                  _: 1
                                }, 8, ["onClick"])
                              ]),
                              _: 2
                            }, 1024)
                          ]);
                        }), 128)),
                        vue.createVNode(_component_n_button, {
                          dashed: "",
                          block: "",
                          onClick: addAction,
                          style: { "margin-top": "8px" }
                        }, {
                          icon: vue.withCtx(() => [
                            vue.createVNode(_component_n_icon, null, {
                              default: vue.withCtx(() => [
                                (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Add))))
                              ]),
                              _: 1
                            })
                          ]),
                          default: vue.withCtx(() => [
                            _cache[19] || (_cache[19] = vue.createTextVNode(" 添加动作 ", -1))
                          ]),
                          _: 1
                        })
                      ])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show", "title"])
        ]);
      };
    }
  });
  const ReviewRules = /* @__PURE__ */ _export_sfc(_sfc_main$3, [["__scopeId", "data-v-0ef816e9"]]);
  const _hoisted_1$2 = { class: "feedback-management-page" };
  const _hoisted_2$2 = { class: "page-header-wrapper" };
  const _hoisted_3$2 = { class: "page-content" };
  const _hoisted_4$2 = { class: "table-container" };
  const _hoisted_5$2 = { class: "pagination-container" };
  const _hoisted_6 = {
    key: 0,
    class: "feedback-detail"
  };
  const _hoisted_7 = { class: "feedback-content" };
  const _hoisted_8 = { class: "handle-comment" };
  const _sfc_main$2 = /* @__PURE__ */ vue.defineComponent({
    __name: "FeedbackManagement",
    setup(__props) {
      const Refresh = useIcon("RefreshOutline");
      const Eye = useIcon("EyeOutline");
      const CheckmarkCircleOutline = useIcon("CheckmarkCircleOutline");
      const CloseCircleOutline = useIcon("CloseCircleOutline");
      const message = useMessage();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const processLoading = vue.ref(false);
      const closeLoading = vue.ref(false);
      const feedbacks = vue.ref([]);
      const showDetailDrawer = vue.ref(false);
      const currentFeedback = vue.ref(null);
      const showProcessModal = vue.ref(false);
      const showCloseModal = vue.ref(false);
      const showAdvanced = vue.ref(false);
      const filters = vue.ref({
        keyword: "",
        feedbackType: null,
        status: null,
        severity: null,
        pluginId: ""
      });
      const pagination = vue.reactive({
        page: 1,
        pageSize: 20,
        itemCount: 0,
        pageSizes: [10, 20, 50, 100]
      });
      const handlePageChange = (page) => {
        pagination.page = page;
        loadFeedbacks();
      };
      const handlePageSizeChange = (pageSize) => {
        pagination.pageSize = pageSize;
        pagination.page = 1;
        loadFeedbacks();
      };
      const basicFields = [
        {
          key: "feedbackType",
          label: "反馈类型",
          type: "select",
          placeholder: "请选择反馈类型",
          options: [
            { label: "全部", value: null },
            { label: "Bug反馈", value: "BUG" },
            { label: "功能建议", value: "FEATURE" },
            { label: "举报", value: "REPORT" },
            { label: "其他", value: "OTHER" }
          ]
        },
        {
          key: "status",
          label: "处理状态",
          type: "select",
          placeholder: "请选择状态",
          options: [
            { label: "全部", value: null },
            { label: "待处理", value: "OPEN" },
            { label: "处理中", value: "IN_PROGRESS" },
            { label: "已解决", value: "RESOLVED" },
            { label: "已关闭", value: "CLOSED" }
          ]
        },
        {
          key: "severity",
          label: "严重程度",
          type: "select",
          placeholder: "请选择严重程度",
          options: [
            { label: "全部", value: null },
            { label: "低", value: "LOW" },
            { label: "中", value: "MEDIUM" },
            { label: "高", value: "HIGH" },
            { label: "严重", value: "CRITICAL" }
          ]
        },
        {
          key: "pluginId",
          label: "插件ID",
          type: "input",
          placeholder: "请输入插件ID"
        },
        {
          key: "keyword",
          label: "关键词",
          type: "input",
          placeholder: "搜索标题、内容"
        }
      ];
      const processStatusOptions = [
        { label: "处理中", value: "IN_PROGRESS" },
        { label: "已解决", value: "RESOLVED" }
      ];
      const processFormRef = vue.ref(null);
      const processForm = vue.reactive({
        status: "IN_PROGRESS",
        handleComment: ""
      });
      const processFormRules = {
        status: {
          required: true,
          message: "请选择处理状态",
          trigger: "change"
        },
        handleComment: {
          required: true,
          message: "请输入处理意见",
          trigger: "blur"
        }
      };
      const closeForm = vue.reactive({
        comment: ""
      });
      const closeFormRules = {};
      const columns = [
        {
          title: "ID",
          key: "id",
          width: 80
        },
        {
          title: "反馈类型",
          key: "feedbackType",
          width: 100,
          render: (row) => vue.h(
            naiveUi.NTag,
            { type: getFeedbackTypeColor(row.feedbackType), size: "small" },
            { default: () => getFeedbackTypeText(row.feedbackType) }
          )
        },
        {
          title: "标题",
          key: "title",
          width: 200,
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "插件ID",
          key: "pluginId",
          width: 150,
          ellipsis: {
            tooltip: true
          }
        },
        {
          title: "严重程度",
          key: "severity",
          width: 100,
          render: (row) => row.severity ? vue.h(
            naiveUi.NTag,
            { type: getSeverityColor(row.severity), size: "small" },
            { default: () => getSeverityText(row.severity) }
          ) : "-"
        },
        {
          title: "状态",
          key: "status",
          width: 100,
          render: (row) => vue.h(
            naiveUi.NTag,
            { type: getStatusColor(row.status) },
            { default: () => getStatusText(row.status) }
          )
        },
        {
          title: "提交用户",
          key: "username",
          width: 120,
          render: (row) => row.username || row.userId
        },
        {
          title: "提交时间",
          key: "submitTime",
          width: 180,
          render: (row) => formatDateTime(row.submitTime)
        },
        {
          title: "处理人",
          key: "handlerName",
          width: 120,
          render: (row) => row.handlerName || "-"
        },
        {
          title: "操作",
          key: "actions",
          width: 150,
          fixed: "right",
          render: (row) => {
            const NButton = vue.resolveComponent("NButton");
            const NSpace = vue.resolveComponent("NSpace");
            const NIcon = vue.resolveComponent("NIcon");
            const NDropdown = vue.resolveComponent("NDropdown");
            const MoreHorizontal = useIcon("EllipsisHorizontal");
            const moreOptions = [];
            if (row.status === "OPEN") {
              moreOptions.push({
                label: "处理",
                key: "process",
                icon: () => vue.h(NIcon, { component: CheckmarkCircleOutline })
              });
            }
            if (row.status === "OPEN" || row.status === "IN_PROGRESS") {
              moreOptions.push({
                label: "关闭",
                key: "close",
                icon: () => vue.h(NIcon, { component: CloseCircleOutline })
              });
            }
            const handleMoreSelect = (key) => {
              switch (key) {
                case "process":
                  handleProcess(row);
                  break;
                case "close":
                  handleClose(row);
                  break;
              }
            };
            return vue.h(
              NSpace,
              { size: 2, wrap: false },
              {
                default: () => [
                  vue.h(
                    NButton,
                    {
                      size: "small",
                      onClick: () => handleViewDetail(row.id)
                    },
                    {
                      icon: () => vue.h(NIcon, { component: Eye }),
                      default: () => "查看"
                    }
                  ),
                  moreOptions.length > 0 && vue.h(
                    NDropdown,
                    {
                      trigger: "click",
                      options: moreOptions,
                      onSelect: handleMoreSelect
                    },
                    {
                      default: () => vue.h(
                        NButton,
                        {
                          size: "small",
                          quaternary: true
                        },
                        {
                          icon: () => vue.h(NIcon, { component: MoreHorizontal })
                        }
                      )
                    }
                  )
                ]
              }
            );
          }
        }
      ];
      const loadFeedbacks = async () => {
        loading.value = true;
        refreshLoading.value = true;
        try {
          const params = {
            page: pagination.page,
            size: pagination.pageSize,
            keyword: filters.value.keyword || void 0,
            feedbackType: filters.value.feedbackType || void 0,
            status: filters.value.status || void 0,
            severity: filters.value.severity || void 0,
            pluginId: filters.value.pluginId || void 0
          };
          const response = await feedbackApi.getList(params);
          feedbacks.value = response.items;
          pagination.itemCount = response.total;
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      };
      const handleSearch = () => {
        pagination.page = 1;
        loadFeedbacks();
      };
      const handleReset = () => {
        filters.value.keyword = "";
        filters.value.feedbackType = null;
        filters.value.status = null;
        filters.value.severity = null;
        filters.value.pluginId = "";
        pagination.page = 1;
        loadFeedbacks();
      };
      const handleViewDetail = async (id) => {
        const response = await feedbackApi.getDetail(id);
        currentFeedback.value = response;
        showDetailDrawer.value = true;
      };
      const handleProcess = (feedback) => {
        currentFeedback.value = feedback;
        processForm.status = "IN_PROGRESS";
        processForm.handleComment = "";
        showProcessModal.value = true;
      };
      const handleProcessSubmit = async () => {
        if (!processFormRef.value || !currentFeedback.value) return false;
        try {
          await processFormRef.value.validate();
          processLoading.value = true;
          const request = {
            handlerId: "admin",
            // TODO: Get from current user
            handlerName: "管理员",
            // TODO: Get from current user
            handleComment: processForm.handleComment,
            status: processForm.status
          };
          const response = await feedbackApi.process(currentFeedback.value.id, request);
          message.success("处理成功");
          showProcessModal.value = false;
          showDetailDrawer.value = false;
          loadFeedbacks();
          return true;
        } catch (error) {
          return false;
        } finally {
          processLoading.value = false;
        }
      };
      const handleClose = (feedback) => {
        currentFeedback.value = feedback;
        closeForm.comment = "";
        showCloseModal.value = true;
      };
      const handleCloseSubmit = async () => {
        if (!currentFeedback.value) return false;
        closeLoading.value = true;
        try {
          const response = await feedbackApi.close(currentFeedback.value.id, {
            handlerId: "admin",
            // TODO: Get from current user
            handlerName: "管理员",
            // TODO: Get from current user
            comment: closeForm.comment || void 0
          });
          message.success("关闭成功");
          showCloseModal.value = false;
          showDetailDrawer.value = false;
          loadFeedbacks();
          return true;
        } catch (error) {
          return false;
        } finally {
          closeLoading.value = false;
        }
      };
      const getFeedbackTypeColor = (type) => {
        const colorMap = {
          BUG: "error",
          FEATURE: "info",
          REPORT: "warning",
          OTHER: "default"
        };
        return colorMap[type] || "default";
      };
      const getFeedbackTypeText = (type) => {
        const textMap = {
          BUG: "Bug反馈",
          FEATURE: "功能建议",
          REPORT: "举报",
          OTHER: "其他"
        };
        return textMap[type] || type;
      };
      const getSeverityColor = (severity) => {
        const colorMap = {
          LOW: "default",
          MEDIUM: "warning",
          HIGH: "error",
          CRITICAL: "error"
        };
        return colorMap[severity] || "default";
      };
      const getSeverityText = (severity) => {
        const textMap = {
          LOW: "低",
          MEDIUM: "中",
          HIGH: "高",
          CRITICAL: "严重"
        };
        return textMap[severity] || severity;
      };
      const getStatusColor = (status) => {
        const colorMap = {
          OPEN: "warning",
          IN_PROGRESS: "info",
          RESOLVED: "success",
          CLOSED: "default"
        };
        return colorMap[status] || "default";
      };
      const getStatusText = (status) => {
        const textMap = {
          OPEN: "待处理",
          IN_PROGRESS: "处理中",
          RESOLVED: "已解决",
          CLOSED: "已关闭"
        };
        return textMap[status] || status;
      };
      const formatDateTime = (dateTime) => {
        if (!dateTime) return "-";
        const date = new Date(dateTime);
        return date.toLocaleString("zh-CN", {
          year: "numeric",
          month: "2-digit",
          day: "2-digit",
          hour: "2-digit",
          minute: "2-digit"
        });
      };
      vue.onMounted(() => {
        loadFeedbacks();
      });
      return (_ctx, _cache) => {
        const _component_n_icon = vue.resolveComponent("n-icon");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_pagination = vue.resolveComponent("n-pagination");
        const _component_n_descriptions_item = vue.resolveComponent("n-descriptions-item");
        const _component_n_descriptions = vue.resolveComponent("n-descriptions");
        const _component_n_card = vue.resolveComponent("n-card");
        const _component_n_divider = vue.resolveComponent("n-divider");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_n_drawer_content = vue.resolveComponent("n-drawer-content");
        const _component_n_drawer = vue.resolveComponent("n-drawer");
        const _component_n_select = vue.resolveComponent("n-select");
        const _component_n_form_item = vue.resolveComponent("n-form-item");
        const _component_n_input = vue.resolveComponent("n-input");
        const _component_n_form = vue.resolveComponent("n-form");
        const _component_n_modal = vue.resolveComponent("n-modal");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$2, [
          vue.createElementVNode("div", _hoisted_2$2, [
            vue.createVNode(_component_PageHeader, {
              title: "用户反馈管理",
              subtitle: "查看和处理用户提交的反馈和举报"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(_component_n_button, {
                  onClick: loadFeedbacks,
                  loading: refreshLoading.value
                }, {
                  icon: vue.withCtx(() => [
                    vue.createVNode(_component_n_icon, null, {
                      default: vue.withCtx(() => [
                        (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                      ]),
                      _: 1
                    })
                  ]),
                  default: vue.withCtx(() => [
                    _cache[13] || (_cache[13] = vue.createTextVNode(" 刷新 ", -1))
                  ]),
                  _: 1
                }, 8, ["loading"])
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$2, [
            vue.createVNode(_component_FilterPanel, {
              filters: filters.value,
              "onUpdate:filters": _cache[0] || (_cache[0] = ($event) => filters.value = $event),
              "show-advanced": showAdvanced.value,
              "onUpdate:showAdvanced": _cache[1] || (_cache[1] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced"]),
            vue.createElementVNode("div", _hoisted_4$2, [
              vue.createVNode(_component_n_data_table, {
                columns,
                data: feedbacks.value,
                loading: loading.value,
                pagination: false,
                "row-key": (row) => row.id,
                striped: ""
              }, null, 8, ["data", "loading", "row-key"]),
              vue.createElementVNode("div", _hoisted_5$2, [
                vue.createVNode(_component_n_pagination, {
                  page: pagination.page,
                  "onUpdate:page": [
                    _cache[2] || (_cache[2] = ($event) => pagination.page = $event),
                    handlePageChange
                  ],
                  "page-size": pagination.pageSize,
                  "onUpdate:pageSize": [
                    _cache[3] || (_cache[3] = ($event) => pagination.pageSize = $event),
                    handlePageSizeChange
                  ],
                  "item-count": pagination.itemCount,
                  "page-sizes": pagination.pageSizes,
                  "show-size-picker": "",
                  "show-quick-jumper": ""
                }, {
                  prefix: vue.withCtx(({ itemCount }) => [
                    vue.createTextVNode(" 共 " + vue.toDisplayString(itemCount) + " 条 ", 1)
                  ]),
                  _: 1
                }, 8, ["page", "page-size", "item-count", "page-sizes"])
              ])
            ])
          ]),
          vue.createVNode(_component_n_drawer, {
            show: showDetailDrawer.value,
            "onUpdate:show": _cache[7] || (_cache[7] = ($event) => showDetailDrawer.value = $event),
            width: 720,
            placement: "right"
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_drawer_content, {
                title: "反馈详情",
                closable: ""
              }, {
                footer: vue.withCtx(() => [
                  vue.createVNode(_component_n_space, { justify: "end" }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_button, {
                        onClick: _cache[4] || (_cache[4] = ($event) => showDetailDrawer.value = false)
                      }, {
                        default: vue.withCtx(() => [..._cache[14] || (_cache[14] = [
                          vue.createTextVNode("关闭", -1)
                        ])]),
                        _: 1
                      }),
                      currentFeedback.value && currentFeedback.value.status === "OPEN" ? (vue.openBlock(), vue.createBlock(_component_n_button, {
                        key: 0,
                        type: "primary",
                        onClick: _cache[5] || (_cache[5] = ($event) => handleProcess(currentFeedback.value))
                      }, {
                        default: vue.withCtx(() => [..._cache[15] || (_cache[15] = [
                          vue.createTextVNode(" 处理反馈 ", -1)
                        ])]),
                        _: 1
                      })) : vue.createCommentVNode("", true),
                      currentFeedback.value && (currentFeedback.value.status === "OPEN" || currentFeedback.value.status === "IN_PROGRESS") ? (vue.openBlock(), vue.createBlock(_component_n_button, {
                        key: 1,
                        type: "success",
                        onClick: _cache[6] || (_cache[6] = ($event) => handleClose(currentFeedback.value))
                      }, {
                        default: vue.withCtx(() => [..._cache[16] || (_cache[16] = [
                          vue.createTextVNode(" 关闭反馈 ", -1)
                        ])]),
                        _: 1
                      })) : vue.createCommentVNode("", true)
                    ]),
                    _: 1
                  })
                ]),
                default: vue.withCtx(() => [
                  currentFeedback.value ? (vue.openBlock(), vue.createElementBlock("div", _hoisted_6, [
                    vue.createVNode(_component_n_card, {
                      title: "基本信息",
                      bordered: false,
                      class: "detail-section"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_descriptions, {
                          column: 2,
                          "label-placement": "left"
                        }, {
                          default: vue.withCtx(() => [
                            vue.createVNode(_component_n_descriptions_item, { label: "反馈ID" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.id), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(_component_n_descriptions_item, { label: "反馈类型" }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(vue.unref(naiveUi.NTag), {
                                  type: getFeedbackTypeColor(currentFeedback.value.feedbackType)
                                }, {
                                  default: vue.withCtx(() => [
                                    vue.createTextVNode(vue.toDisplayString(getFeedbackTypeText(currentFeedback.value.feedbackType)), 1)
                                  ]),
                                  _: 1
                                }, 8, ["type"])
                              ]),
                              _: 1
                            }),
                            vue.createVNode(_component_n_descriptions_item, { label: "插件ID" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.pluginId), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(_component_n_descriptions_item, { label: "插件版本" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.pluginVersion || "-"), 1)
                              ]),
                              _: 1
                            }),
                            currentFeedback.value.severity ? (vue.openBlock(), vue.createBlock(_component_n_descriptions_item, {
                              key: 0,
                              label: "严重程度"
                            }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(vue.unref(naiveUi.NTag), {
                                  type: getSeverityColor(currentFeedback.value.severity)
                                }, {
                                  default: vue.withCtx(() => [
                                    vue.createTextVNode(vue.toDisplayString(getSeverityText(currentFeedback.value.severity)), 1)
                                  ]),
                                  _: 1
                                }, 8, ["type"])
                              ]),
                              _: 1
                            })) : vue.createCommentVNode("", true),
                            vue.createVNode(_component_n_descriptions_item, { label: "状态" }, {
                              default: vue.withCtx(() => [
                                vue.createVNode(vue.unref(naiveUi.NTag), {
                                  type: getStatusColor(currentFeedback.value.status)
                                }, {
                                  default: vue.withCtx(() => [
                                    vue.createTextVNode(vue.toDisplayString(getStatusText(currentFeedback.value.status)), 1)
                                  ]),
                                  _: 1
                                }, 8, ["type"])
                              ]),
                              _: 1
                            }),
                            vue.createVNode(_component_n_descriptions_item, {
                              label: "提交时间",
                              span: 2
                            }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(formatDateTime(currentFeedback.value.submitTime)), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    }),
                    vue.createVNode(_component_n_card, {
                      title: "用户信息",
                      bordered: false,
                      class: "detail-section"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_descriptions, {
                          column: 2,
                          "label-placement": "left"
                        }, {
                          default: vue.withCtx(() => [
                            vue.createVNode(_component_n_descriptions_item, { label: "用户ID" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.userId), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(_component_n_descriptions_item, { label: "用户名" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.username || "-"), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(_component_n_descriptions_item, {
                              label: "联系方式",
                              span: 2
                            }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.contact || "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    }),
                    vue.createVNode(_component_n_card, {
                      title: "反馈内容",
                      bordered: false,
                      class: "detail-section"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_descriptions, {
                          column: 1,
                          "label-placement": "left"
                        }, {
                          default: vue.withCtx(() => [
                            vue.createVNode(_component_n_descriptions_item, { label: "标题" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.title), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(_component_n_descriptions_item, { label: "详细描述" }, {
                              default: vue.withCtx(() => [
                                vue.createElementVNode("div", _hoisted_7, vue.toDisplayString(currentFeedback.value.content), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        currentFeedback.value.attachments && currentFeedback.value.attachments.length > 0 ? (vue.openBlock(), vue.createElementBlock(vue.Fragment, { key: 0 }, [
                          vue.createVNode(_component_n_divider),
                          vue.createVNode(_component_n_descriptions, {
                            column: 1,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(_component_n_descriptions_item, { label: "附件" }, {
                                default: vue.withCtx(() => [
                                  vue.createVNode(_component_n_space, { vertical: "" }, {
                                    default: vue.withCtx(() => [
                                      (vue.openBlock(true), vue.createElementBlock(vue.Fragment, null, vue.renderList(currentFeedback.value.attachments, (attachment, index2) => {
                                        return vue.openBlock(), vue.createBlock(vue.unref(naiveUi.NTag), {
                                          key: index2,
                                          size: "small"
                                        }, {
                                          default: vue.withCtx(() => [
                                            vue.createTextVNode(vue.toDisplayString(attachment), 1)
                                          ]),
                                          _: 2
                                        }, 1024);
                                      }), 128))
                                    ]),
                                    _: 1
                                  })
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          })
                        ], 64)) : vue.createCommentVNode("", true)
                      ]),
                      _: 1
                    }),
                    currentFeedback.value.handlerId ? (vue.openBlock(), vue.createBlock(_component_n_card, {
                      key: 0,
                      title: "处理信息",
                      bordered: false,
                      class: "detail-section"
                    }, {
                      default: vue.withCtx(() => [
                        vue.createVNode(_component_n_descriptions, {
                          column: 2,
                          "label-placement": "left"
                        }, {
                          default: vue.withCtx(() => [
                            vue.createVNode(_component_n_descriptions_item, { label: "处理人ID" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.handlerId), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(_component_n_descriptions_item, { label: "处理人" }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.handlerName || "-"), 1)
                              ]),
                              _: 1
                            }),
                            vue.createVNode(_component_n_descriptions_item, {
                              label: "处理时间",
                              span: 2
                            }, {
                              default: vue.withCtx(() => [
                                vue.createTextVNode(vue.toDisplayString(currentFeedback.value.handleTime ? formatDateTime(currentFeedback.value.handleTime) : "-"), 1)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }),
                        currentFeedback.value.handleComment ? (vue.openBlock(), vue.createElementBlock(vue.Fragment, { key: 0 }, [
                          vue.createVNode(_component_n_divider),
                          vue.createVNode(_component_n_descriptions, {
                            column: 1,
                            "label-placement": "left"
                          }, {
                            default: vue.withCtx(() => [
                              vue.createVNode(_component_n_descriptions_item, { label: "处理意见" }, {
                                default: vue.withCtx(() => [
                                  vue.createElementVNode("div", _hoisted_8, vue.toDisplayString(currentFeedback.value.handleComment), 1)
                                ]),
                                _: 1
                              })
                            ]),
                            _: 1
                          })
                        ], 64)) : vue.createCommentVNode("", true)
                      ]),
                      _: 1
                    })) : vue.createCommentVNode("", true)
                  ])) : vue.createCommentVNode("", true)
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["show"]),
          vue.createVNode(_component_n_modal, {
            show: showProcessModal.value,
            "onUpdate:show": _cache[10] || (_cache[10] = ($event) => showProcessModal.value = $event),
            preset: "dialog",
            title: "处理反馈",
            "positive-text": "确定",
            "negative-text": "取消",
            "positive-button-props": { loading: processLoading.value },
            onPositiveClick: handleProcessSubmit
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "processFormRef",
                ref: processFormRef,
                model: processForm,
                rules: processFormRules,
                "label-placement": "left",
                "label-width": "100px"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "处理状态",
                    path: "status"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_select, {
                        value: processForm.status,
                        "onUpdate:value": _cache[8] || (_cache[8] = ($event) => processForm.status = $event),
                        options: processStatusOptions,
                        placeholder: "请选择处理状态"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "处理意见",
                    path: "handleComment"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: processForm.handleComment,
                        "onUpdate:value": _cache[9] || (_cache[9] = ($event) => processForm.handleComment = $event),
                        type: "textarea",
                        rows: 4,
                        placeholder: "请输入处理意见"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show", "positive-button-props"]),
          vue.createVNode(_component_n_modal, {
            show: showCloseModal.value,
            "onUpdate:show": _cache[12] || (_cache[12] = ($event) => showCloseModal.value = $event),
            preset: "dialog",
            title: "关闭反馈",
            "positive-text": "确定",
            "negative-text": "取消",
            "positive-button-props": { loading: closeLoading.value },
            onPositiveClick: handleCloseSubmit
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                model: closeForm,
                rules: closeFormRules,
                "label-placement": "left",
                "label-width": "100px"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "关闭说明",
                    path: "comment"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: closeForm.comment,
                        "onUpdate:value": _cache[11] || (_cache[11] = ($event) => closeForm.comment = $event),
                        type: "textarea",
                        rows: 4,
                        placeholder: "请输入关闭说明（可选）"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show", "positive-button-props"])
        ]);
      };
    }
  });
  const FeedbackManagement = /* @__PURE__ */ _export_sfc(_sfc_main$2, [["__scopeId", "data-v-bf346ffe"]]);
  const _hoisted_1$1 = { class: "permission-request-review-page" };
  const _hoisted_2$1 = { class: "page-header-wrapper" };
  const _hoisted_3$1 = { class: "page-content" };
  const _hoisted_4$1 = { class: "table-container" };
  const _hoisted_5$1 = { class: "pagination-container" };
  const _sfc_main$1 = /* @__PURE__ */ vue.defineComponent({
    __name: "PermissionRequestReview",
    setup(__props) {
      const CheckmarkCircle = useIcon("CheckmarkCircleOutline");
      const CloseCircle = useIcon("CloseCircleOutline");
      const Refresh = useIcon("RefreshOutline");
      const message = naiveUi.useMessage();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const tableData = vue.ref([]);
      const currentRequest = vue.ref(null);
      const pagination = vue.reactive({
        page: 1,
        pageSize: 20,
        total: 0
      });
      const filters = vue.reactive({
        pluginId: "",
        status: ""
      });
      const showAdvanced = vue.ref(false);
      const showApproveModal = vue.ref(false);
      const approveFormRef = vue.ref(null);
      const approveForm = vue.reactive({
        comment: ""
      });
      const approveRules = {};
      const showRejectModal = vue.ref(false);
      const rejectFormRef = vue.ref(null);
      const rejectForm = vue.reactive({
        reason: ""
      });
      const rejectRules = {
        reason: [
          { required: true, message: "请输入拒绝原因", trigger: "blur" }
        ]
      };
      const basicFields = [
        {
          key: "pluginId",
          label: "插件ID",
          type: "input",
          placeholder: "请输入插件ID"
        },
        {
          key: "status",
          label: "审核状态",
          type: "select",
          placeholder: "请选择状态",
          options: [
            { label: "全部", value: "" },
            { label: "待审核", value: "PENDING" },
            { label: "已批准", value: "APPROVED" },
            { label: "已拒绝", value: "REJECTED" },
            { label: "已取消", value: "CANCELLED" }
          ]
        }
      ];
      const advancedFields = [];
      const columns = [
        {
          title: "插件ID",
          key: "pluginId",
          width: 150,
          ellipsis: { tooltip: true }
        },
        {
          title: "表名",
          key: "tableName",
          width: 200,
          ellipsis: { tooltip: true }
        },
        {
          title: "申请操作",
          key: "requestedOperations",
          width: 200,
          ellipsis: { tooltip: true }
        },
        {
          title: "只读",
          key: "isReadonly",
          width: 80,
          render: (row) => {
            return row.isReadonly ? "是" : "否";
          }
        },
        {
          title: "申请原因",
          key: "reason",
          width: 200,
          ellipsis: { tooltip: true }
        },
        {
          title: "申请人",
          key: "applicantName",
          width: 120
        },
        {
          title: "状态",
          key: "status",
          width: 100,
          render: (row) => {
            const statusMap = {
              PENDING: { label: "待审核", type: "warning" },
              APPROVED: { label: "已批准", type: "success" },
              REJECTED: { label: "已拒绝", type: "error" },
              CANCELLED: { label: "已取消", type: "default" }
            };
            const config = statusMap[row.status || "PENDING"];
            return vue.h(naiveUi.NTag, { type: config.type, size: "small" }, { default: () => config.label });
          }
        },
        {
          title: "审核人",
          key: "reviewerName",
          width: 120
        },
        {
          title: "审核时间",
          key: "reviewTime",
          width: 160,
          render: (row) => row.reviewTime ? formatDateTime(row.reviewTime) : "-"
        },
        {
          title: "申请时间",
          key: "createTime",
          width: 160,
          render: (row) => formatDateTime(row.createTime)
        },
        {
          title: "操作",
          key: "actions",
          width: 200,
          fixed: "right",
          render: (row) => {
            const NButton2 = vue.resolveComponent("NButton");
            const NSpace2 = vue.resolveComponent("NSpace");
            const NIcon2 = vue.resolveComponent("NIcon");
            const NDropdown = vue.resolveComponent("NDropdown");
            const MoreHorizontal = useIcon("EllipsisHorizontal");
            const Eye = useIcon("EyeOutline");
            const moreOptions = [];
            if (row.status === "PENDING") {
              moreOptions.push({
                label: "批准",
                key: "approve",
                icon: () => vue.h(NIcon2, { component: CheckmarkCircle })
              });
              moreOptions.push({
                label: "拒绝",
                key: "reject",
                icon: () => vue.h(NIcon2, { component: CloseCircle })
              });
            }
            const handleMoreSelect = (key) => {
              switch (key) {
                case "approve":
                  handleApprove(row);
                  break;
                case "reject":
                  handleReject(row);
                  break;
              }
            };
            return vue.h(
              NSpace2,
              { size: 2, wrap: false },
              {
                default: () => [
                  vue.h(
                    NButton2,
                    {
                      size: "small",
                      onClick: () => {
                      }
                      // TODO: 实现查看详情
                    },
                    {
                      icon: () => vue.h(NIcon2, { component: Eye }),
                      default: () => "查看"
                    }
                  ),
                  moreOptions.length > 0 && vue.h(
                    NDropdown,
                    {
                      trigger: "click",
                      options: moreOptions,
                      onSelect: handleMoreSelect
                    },
                    {
                      default: () => vue.h(
                        NButton2,
                        {
                          size: "small",
                          quaternary: true
                        },
                        {
                          icon: () => vue.h(NIcon2, { component: MoreHorizontal })
                        }
                      )
                    }
                  )
                ]
              }
            );
          }
        }
      ];
      const loadData = async () => {
        loading.value = true;
        try {
          const response = await permissionRequestApi.getList({
            pluginId: filters.pluginId || void 0,
            status: filters.status || void 0
          });
          tableData.value = response;
          pagination.total = response.length;
        } finally {
          loading.value = false;
        }
      };
      const handleSearch = () => {
        pagination.page = 1;
        loadData();
      };
      const handleReset = () => {
        filters.pluginId = "";
        filters.status = "";
        pagination.page = 1;
        loadData();
      };
      const handlePageChange = (page) => {
        pagination.page = page;
        loadData();
      };
      const handlePageSizeChange = (pageSize) => {
        pagination.pageSize = pageSize;
        pagination.page = 1;
        loadData();
      };
      const handleApprove = (row) => {
        currentRequest.value = row;
        approveForm.comment = "";
        showApproveModal.value = true;
      };
      const handleApproveSubmit = async () => {
        if (!currentRequest.value || !currentRequest.value.id) return;
        const request = {
          comment: approveForm.comment,
          reviewerId: "admin",
          // TODO: 从用户上下文获取
          reviewerName: "管理员"
          // TODO: 从用户上下文获取
        };
        await permissionRequestApi.approve(currentRequest.value.id, request);
        message.success("批准成功");
        showApproveModal.value = false;
        loadData();
      };
      const handleReject = (row) => {
        currentRequest.value = row;
        rejectForm.reason = "";
        showRejectModal.value = true;
      };
      const handleRejectSubmit = async () => {
        if (!rejectFormRef.value) return;
        await rejectFormRef.value.validate(async (errors) => {
          if (!errors && currentRequest.value && currentRequest.value.id) {
            const request = {
              reason: rejectForm.reason,
              reviewerId: "admin",
              // TODO: 从用户上下文获取
              reviewerName: "管理员"
              // TODO: 从用户上下文获取
            };
            await permissionRequestApi.reject(currentRequest.value.id, request);
            message.success("拒绝成功");
            showRejectModal.value = false;
            loadData();
          }
        });
      };
      vue.onMounted(() => {
        loadData();
      });
      const formatDateTime = (dateTime) => {
        if (!dateTime) return "-";
        const date = new Date(dateTime);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        const seconds = String(date.getSeconds()).padStart(2, "0");
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      };
      return (_ctx, _cache) => {
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_pagination = vue.resolveComponent("n-pagination");
        const _component_n_input = vue.resolveComponent("n-input");
        const _component_n_form_item = vue.resolveComponent("n-form-item");
        const _component_n_form = vue.resolveComponent("n-form");
        const _component_n_modal = vue.resolveComponent("n-modal");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1$1, [
          vue.createElementVNode("div", _hoisted_2$1, [
            vue.createVNode(_component_PageHeader, {
              title: "权限申请审核",
              subtitle: "审核插件对系统表的访问权限申请"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(vue.unref(naiveUi.NSpace), null, {
                  default: vue.withCtx(() => [
                    vue.createVNode(vue.unref(naiveUi.NButton), {
                      onClick: loadData,
                      loading: refreshLoading.value
                    }, {
                      icon: vue.withCtx(() => [
                        vue.createVNode(vue.unref(naiveUi.NIcon), null, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                          ]),
                          _: 1
                        })
                      ]),
                      default: vue.withCtx(() => [
                        _cache[8] || (_cache[8] = vue.createTextVNode(" 刷新 ", -1))
                      ]),
                      _: 1
                    }, 8, ["loading"])
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3$1, [
            vue.createVNode(_component_FilterPanel, {
              filters,
              "show-advanced": showAdvanced.value,
              "onUpdate:filters": _cache[0] || (_cache[0] = ($event) => Object.assign(filters, $event)),
              "onUpdate:showAdvanced": _cache[1] || (_cache[1] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields,
              "advanced-fields": advancedFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced"]),
            vue.createElementVNode("div", _hoisted_4$1, [
              vue.createVNode(_component_n_data_table, {
                columns,
                data: tableData.value,
                loading: loading.value,
                pagination: false,
                "row-key": (row) => row.id,
                striped: ""
              }, null, 8, ["data", "loading", "row-key"]),
              vue.createElementVNode("div", _hoisted_5$1, [
                vue.createVNode(_component_n_pagination, {
                  page: pagination.page,
                  "onUpdate:page": [
                    _cache[2] || (_cache[2] = ($event) => pagination.page = $event),
                    handlePageChange
                  ],
                  "page-size": pagination.pageSize,
                  "onUpdate:pageSize": [
                    _cache[3] || (_cache[3] = ($event) => pagination.pageSize = $event),
                    handlePageSizeChange
                  ],
                  "item-count": pagination.total,
                  "page-sizes": [10, 20, 50, 100],
                  "show-size-picker": "",
                  "show-quick-jumper": ""
                }, null, 8, ["page", "page-size", "item-count"])
              ])
            ])
          ]),
          vue.createVNode(_component_n_modal, {
            show: showApproveModal.value,
            "onUpdate:show": _cache[5] || (_cache[5] = ($event) => showApproveModal.value = $event),
            preset: "dialog",
            title: "批准权限申请",
            "positive-text": "确认批准",
            "negative-text": "取消",
            onPositiveClick: handleApproveSubmit
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "approveFormRef",
                ref: approveFormRef,
                model: approveForm,
                rules: approveRules,
                "label-placement": "left",
                "label-width": "100px"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "审核意见",
                    path: "comment"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: approveForm.comment,
                        "onUpdate:value": _cache[4] || (_cache[4] = ($event) => approveForm.comment = $event),
                        type: "textarea",
                        rows: 4,
                        placeholder: "请输入审核意见（可选）"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show"]),
          vue.createVNode(_component_n_modal, {
            show: showRejectModal.value,
            "onUpdate:show": _cache[7] || (_cache[7] = ($event) => showRejectModal.value = $event),
            preset: "dialog",
            title: "拒绝权限申请",
            "positive-text": "确认拒绝",
            "negative-text": "取消",
            onPositiveClick: handleRejectSubmit
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "rejectFormRef",
                ref: rejectFormRef,
                model: rejectForm,
                rules: rejectRules,
                "label-placement": "left",
                "label-width": "100px"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "拒绝原因",
                    path: "reason"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: rejectForm.reason,
                        "onUpdate:value": _cache[6] || (_cache[6] = ($event) => rejectForm.reason = $event),
                        type: "textarea",
                        rows: 4,
                        placeholder: "请输入拒绝原因（必填）"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show"])
        ]);
      };
    }
  });
  const PermissionRequestReview = /* @__PURE__ */ _export_sfc(_sfc_main$1, [["__scopeId", "data-v-eb0bd909"]]);
  const _hoisted_1 = { class: "plugin-table-permission-management-page" };
  const _hoisted_2 = { class: "page-header-wrapper" };
  const _hoisted_3 = { class: "page-content" };
  const _hoisted_4 = { class: "table-container" };
  const _hoisted_5 = { class: "pagination-container" };
  const _sfc_main = /* @__PURE__ */ vue.defineComponent({
    __name: "PluginTablePermissionManagement",
    setup(__props) {
      const Add = useIcon("AddOutline");
      const Refresh = useIcon("RefreshOutline");
      const Edit = useIcon("CreateOutline");
      const Delete = useIcon("TrashOutline");
      const CheckmarkCircle = useIcon("CheckmarkCircleOutline");
      const CloseCircle = useIcon("CloseCircleOutline");
      const message = useMessage();
      const loading = vue.ref(false);
      const refreshLoading = vue.ref(false);
      const submitting = vue.ref(false);
      const tableData = vue.ref([]);
      const showFormModal = vue.ref(false);
      const formMode = vue.ref("create");
      const formRef = vue.ref(null);
      const formData = vue.ref({
        enabled: true,
        isReadonly: true
      });
      const selectedOperations = vue.ref([]);
      const operationOptions = [
        { label: "SELECT", value: "SELECT" },
        { label: "INSERT", value: "INSERT" },
        { label: "UPDATE", value: "UPDATE" },
        { label: "DELETE", value: "DELETE" }
      ];
      const formRules = {
        pluginId: {
          required: true,
          message: "请输入插件ID",
          trigger: "blur"
        },
        tableName: {
          required: true,
          message: "请输入表名",
          trigger: "blur"
        }
      };
      const showAdvanced = vue.ref(false);
      const filters = vue.ref({
        pluginId: "",
        tableName: "",
        enabled: null
      });
      const pagination = vue.reactive({
        page: 1,
        pageSize: 20,
        itemCount: 0,
        pageSizes: [10, 20, 50, 100]
      });
      const basicFields = [
        {
          key: "pluginId",
          label: "插件ID",
          type: "input",
          placeholder: "请输入插件ID"
        },
        {
          key: "tableName",
          label: "表名",
          type: "input",
          placeholder: "请输入表名"
        }
      ];
      const advancedFields = [
        {
          key: "enabled",
          label: "状态",
          type: "select",
          placeholder: "全部",
          options: [
            { label: "启用", value: true },
            { label: "禁用", value: false }
          ]
        }
      ];
      const columns = [
        {
          title: "ID",
          key: "id",
          width: 80
        },
        {
          title: "插件ID",
          key: "pluginId",
          width: 200
        },
        {
          title: "表名",
          key: "tableName",
          width: 200
        },
        {
          title: "允许的操作",
          key: "allowedOperations",
          width: 200,
          render: (row) => {
            return row.allowedOperations || "全部";
          }
        },
        {
          title: "只读",
          key: "isReadonly",
          width: 100,
          render: (row) => {
            return vue.h("n-tag", {
              type: row.isReadonly ? "warning" : "success",
              size: "small"
            }, { default: () => row.isReadonly ? "是" : "否" });
          }
        },
        {
          title: "状态",
          key: "enabled",
          width: 100,
          render: (row) => {
            return vue.h("n-tag", {
              type: row.enabled ? "success" : "default",
              size: "small"
            }, { default: () => row.enabled ? "启用" : "禁用" });
          }
        },
        {
          title: "描述",
          key: "description",
          ellipsis: true
        },
        {
          title: "操作",
          key: "actions",
          width: 200,
          fixed: "right",
          render: (row) => {
            const NButton = vue.resolveComponent("NButton");
            const NSpace = vue.resolveComponent("NSpace");
            const NIcon = vue.resolveComponent("NIcon");
            const NDropdown = vue.resolveComponent("NDropdown");
            vue.resolveComponent("NPopconfirm");
            const MoreHorizontal = useIcon("EllipsisHorizontal");
            const Eye = useIcon("EyeOutline");
            const moreOptions = [
              {
                label: "编辑",
                key: "edit",
                icon: () => vue.h(NIcon, { component: Edit })
              },
              {
                label: row.enabled ? "禁用" : "启用",
                key: "toggle",
                icon: () => vue.h(NIcon, { component: row.enabled ? CloseCircle : CheckmarkCircle })
              },
              {
                label: "删除",
                key: "delete",
                icon: () => vue.h(NIcon, { component: Delete })
              }
            ];
            const handleMoreSelect = (key) => {
              switch (key) {
                case "edit":
                  handleEdit(row);
                  break;
                case "toggle":
                  handleToggleEnabled(row);
                  break;
                case "delete":
                  handleDelete(row);
                  break;
              }
            };
            return vue.h(
              NSpace,
              { size: 2, wrap: false },
              {
                default: () => [
                  vue.h(
                    NButton,
                    {
                      size: "small",
                      onClick: () => {
                      }
                      // TODO: 实现查看详情
                    },
                    {
                      icon: () => vue.h(NIcon, { component: Eye }),
                      default: () => "查看"
                    }
                  ),
                  vue.h(
                    NDropdown,
                    {
                      trigger: "click",
                      options: moreOptions,
                      onSelect: handleMoreSelect
                    },
                    {
                      default: () => vue.h(
                        NButton,
                        {
                          size: "small",
                          quaternary: true
                        },
                        {
                          icon: () => vue.h(NIcon, { component: MoreHorizontal })
                        }
                      )
                    }
                  )
                ]
              }
            );
          }
        }
      ];
      vue.watch(() => formData.value.allowedOperations, (val) => {
        if (val) {
          selectedOperations.value = val.split(",").map((s) => s.trim());
        } else {
          selectedOperations.value = [];
        }
      }, { immediate: true });
      const loadData = async () => {
        loading.value = true;
        try {
          const params = {};
          if (filters.value.pluginId) {
            params.pluginId = filters.value.pluginId;
          }
          if (filters.value.tableName) {
            params.tableName = filters.value.tableName;
          }
          const response = await tablePermissionApi.getList(params);
          let data = response;
          if (filters.value.enabled !== null) {
            data = data.filter((item) => item.enabled === filters.value.enabled);
          }
          tableData.value = data;
          pagination.itemCount = data.length;
        } finally {
          loading.value = false;
          refreshLoading.value = false;
        }
      };
      const handleSearch = () => {
        pagination.page = 1;
        loadData();
      };
      const handleReset = () => {
        filters.value = {
          pluginId: "",
          tableName: "",
          enabled: null
        };
        pagination.page = 1;
        loadData();
      };
      const handleCreate = () => {
        formMode.value = "create";
        formData.value = {
          enabled: true,
          isReadonly: true
        };
        selectedOperations.value = [];
        showFormModal.value = true;
      };
      const handleEdit = (row) => {
        formMode.value = "edit";
        formData.value = { ...row };
        if (row.allowedOperations) {
          selectedOperations.value = row.allowedOperations.split(",").map((s) => s.trim());
        } else {
          selectedOperations.value = [];
        }
        showFormModal.value = true;
      };
      const handleDelete = async (row) => {
        if (!row.id) return;
        await tablePermissionApi.delete(row.id);
        message.success("删除成功");
        loadData();
      };
      const handleToggleEnabled = async (row) => {
        if (!row.id) return;
        await tablePermissionApi.setEnabled(row.id, !row.enabled);
        message.success(`${row.enabled ? "禁用" : "启用"}成功`);
        loadData();
      };
      const handleFormSubmit = async () => {
        if (!formRef.value) return;
        await formRef.value.validate(async (errors) => {
          var _a;
          if (!errors) {
            submitting.value = true;
            try {
              const data = {
                ...formData.value,
                allowedOperations: selectedOperations.value.length > 0 ? selectedOperations.value.join(",") : void 0,
                tableName: (_a = formData.value.tableName) == null ? void 0 : _a.toLowerCase()
              };
              let response;
              if (formMode.value === "create") {
                response = await tablePermissionApi.create(data);
              } else {
                if (data.id) {
                  response = await tablePermissionApi.update(data.id, data);
                }
              }
              message.success(formMode.value === "create" ? "创建成功" : "更新成功");
              showFormModal.value = false;
              loadData();
            } finally {
              submitting.value = false;
            }
          }
        });
      };
      const handlePageChange = (page) => {
        pagination.page = page;
        loadData();
      };
      const handlePageSizeChange = (pageSize) => {
        pagination.pageSize = pageSize;
        pagination.page = 1;
        loadData();
      };
      vue.onMounted(() => {
        loadData();
      });
      return (_ctx, _cache) => {
        const _component_n_icon = vue.resolveComponent("n-icon");
        const _component_n_button = vue.resolveComponent("n-button");
        const _component_n_space = vue.resolveComponent("n-space");
        const _component_PageHeader = vue.resolveComponent("PageHeader");
        const _component_FilterPanel = vue.resolveComponent("FilterPanel");
        const _component_n_data_table = vue.resolveComponent("n-data-table");
        const _component_n_pagination = vue.resolveComponent("n-pagination");
        const _component_n_input = vue.resolveComponent("n-input");
        const _component_n_form_item = vue.resolveComponent("n-form-item");
        const _component_n_select = vue.resolveComponent("n-select");
        const _component_n_switch = vue.resolveComponent("n-switch");
        const _component_n_form = vue.resolveComponent("n-form");
        const _component_n_modal = vue.resolveComponent("n-modal");
        return vue.openBlock(), vue.createElementBlock("div", _hoisted_1, [
          vue.createElementVNode("div", _hoisted_2, [
            vue.createVNode(_component_PageHeader, {
              title: "插件表权限管理",
              subtitle: "配置插件对系统表的特殊访问权限"
            }, {
              actions: vue.withCtx(() => [
                vue.createVNode(_component_n_space, null, {
                  default: vue.withCtx(() => [
                    vue.createVNode(_component_n_button, {
                      type: "primary",
                      onClick: handleCreate
                    }, {
                      icon: vue.withCtx(() => [
                        vue.createVNode(_component_n_icon, null, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Add))))
                          ]),
                          _: 1
                        })
                      ]),
                      default: vue.withCtx(() => [
                        _cache[12] || (_cache[12] = vue.createTextVNode(" 新增权限 ", -1))
                      ]),
                      _: 1
                    }),
                    vue.createVNode(_component_n_button, {
                      onClick: loadData,
                      loading: refreshLoading.value
                    }, {
                      icon: vue.withCtx(() => [
                        vue.createVNode(_component_n_icon, null, {
                          default: vue.withCtx(() => [
                            (vue.openBlock(), vue.createBlock(vue.resolveDynamicComponent(vue.unref(Refresh))))
                          ]),
                          _: 1
                        })
                      ]),
                      default: vue.withCtx(() => [
                        _cache[13] || (_cache[13] = vue.createTextVNode(" 刷新 ", -1))
                      ]),
                      _: 1
                    }, 8, ["loading"])
                  ]),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          vue.createElementVNode("div", _hoisted_3, [
            vue.createVNode(_component_FilterPanel, {
              filters: filters.value,
              "onUpdate:filters": _cache[0] || (_cache[0] = ($event) => filters.value = $event),
              "show-advanced": showAdvanced.value,
              "onUpdate:showAdvanced": _cache[1] || (_cache[1] = ($event) => showAdvanced.value = $event),
              "basic-fields": basicFields,
              "advanced-fields": advancedFields,
              onSearch: handleSearch,
              onReset: handleReset
            }, null, 8, ["filters", "show-advanced"]),
            vue.createElementVNode("div", _hoisted_4, [
              vue.createVNode(_component_n_data_table, {
                columns,
                data: tableData.value,
                loading: loading.value,
                pagination: false,
                "row-key": (row) => row.id,
                striped: ""
              }, null, 8, ["data", "loading", "row-key"]),
              vue.createElementVNode("div", _hoisted_5, [
                vue.createVNode(_component_n_pagination, {
                  page: pagination.page,
                  "onUpdate:page": [
                    _cache[2] || (_cache[2] = ($event) => pagination.page = $event),
                    handlePageChange
                  ],
                  "page-size": pagination.pageSize,
                  "onUpdate:pageSize": [
                    _cache[3] || (_cache[3] = ($event) => pagination.pageSize = $event),
                    handlePageSizeChange
                  ],
                  "item-count": pagination.itemCount,
                  "page-sizes": pagination.pageSizes,
                  "show-size-picker": "",
                  "show-quick-jumper": ""
                }, {
                  prefix: vue.withCtx(({ itemCount }) => [
                    vue.createTextVNode(" 共 " + vue.toDisplayString(itemCount) + " 条 ", 1)
                  ]),
                  _: 1
                }, 8, ["page", "page-size", "item-count", "page-sizes"])
              ])
            ])
          ]),
          vue.createVNode(_component_n_modal, {
            show: showFormModal.value,
            "onUpdate:show": _cache[10] || (_cache[10] = ($event) => showFormModal.value = $event),
            title: formMode.value === "create" ? "新增权限配置" : "编辑权限配置",
            preset: "dialog",
            style: { "width": "600px" },
            "positive-button-props": { loading: submitting.value },
            onPositiveClick: handleFormSubmit,
            onNegativeClick: _cache[11] || (_cache[11] = ($event) => showFormModal.value = false)
          }, {
            default: vue.withCtx(() => [
              vue.createVNode(_component_n_form, {
                ref_key: "formRef",
                ref: formRef,
                model: formData.value,
                rules: formRules,
                "label-placement": "left",
                "label-width": "120px"
              }, {
                default: vue.withCtx(() => [
                  vue.createVNode(_component_n_form_item, {
                    label: "插件ID",
                    path: "pluginId"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: formData.value.pluginId,
                        "onUpdate:value": _cache[4] || (_cache[4] = ($event) => formData.value.pluginId = $event),
                        placeholder: "请输入插件ID",
                        disabled: formMode.value === "edit"
                      }, null, 8, ["value", "disabled"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "表名",
                    path: "tableName"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: formData.value.tableName,
                        "onUpdate:value": _cache[5] || (_cache[5] = ($event) => formData.value.tableName = $event),
                        placeholder: "请输入表名（小写）"
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "允许的操作",
                    path: "allowedOperations"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_select, {
                        value: selectedOperations.value,
                        "onUpdate:value": _cache[6] || (_cache[6] = ($event) => selectedOperations.value = $event),
                        multiple: "",
                        placeholder: "选择允许的操作（不选则允许全部）",
                        options: operationOptions
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "只读模式",
                    path: "isReadonly"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_switch, {
                        value: formData.value.isReadonly,
                        "onUpdate:value": _cache[7] || (_cache[7] = ($event) => formData.value.isReadonly = $event)
                      }, null, 8, ["value"]),
                      _cache[14] || (_cache[14] = vue.createElementVNode("span", { style: { "margin-left": "8px", "color": "#666" } }, " 开启后只允许 SELECT 操作 ", -1))
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "描述",
                    path: "description"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_input, {
                        value: formData.value.description,
                        "onUpdate:value": _cache[8] || (_cache[8] = ($event) => formData.value.description = $event),
                        type: "textarea",
                        placeholder: "请输入描述",
                        rows: 3
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  }),
                  vue.createVNode(_component_n_form_item, {
                    label: "启用",
                    path: "enabled"
                  }, {
                    default: vue.withCtx(() => [
                      vue.createVNode(_component_n_switch, {
                        value: formData.value.enabled,
                        "onUpdate:value": _cache[9] || (_cache[9] = ($event) => formData.value.enabled = $event)
                      }, null, 8, ["value"])
                    ]),
                    _: 1
                  })
                ]),
                _: 1
              }, 8, ["model"])
            ]),
            _: 1
          }, 8, ["show", "title", "positive-button-props"])
        ]);
      };
    }
  });
  const PluginTablePermissionManagement = /* @__PURE__ */ _export_sfc(_sfc_main, [["__scopeId", "data-v-d2eaf243"]]);
  const defaultConfig = {
    enabled: true,
    debug: false
  };
  const index = (bridge, properties) => {
    const config = {
      ...defaultConfig,
      ...properties || {}
    };
    const { ui, app } = bridge || {};
    return {
      id: "appstore-admin",
      name: "插件商店管理",
      version: "1.0.0",
      description: "插件商店管理后台，提供插件审核、上架、下架、版本管理等功能",
      author: {
        name: "Gress Team"
      },
      icon: "shield-checkmark-outline",
      permissions: [
        "NETWORK_ACCESS",
        "ROUTER_REGISTER",
        "ROUTER_NAVIGATE",
        "COMPONENT_REGISTER",
        "DATA_READ",
        "DATA_WRITE",
        "STORAGE_READ",
        "STORAGE_WRITE",
        "UI_MENU"
      ],
      loadStrategy: "lazy",
      /**
       * 组件注册表（名称 -> 组件实例）
       *
       * - 后端 plugin-ui.yml 中只写组件名称（如 PluginAdminLayout / PluginSubmissions）
       * - 宿主通过 PluginRuntime 获取 manifest.components 后按名称查找组件：
       *   const runtime = getPluginRuntime()
       *   const plugin = runtime.get('appstore-admin')
       *   const comp = plugin?.manifest.components?.['PluginAdminLayout']
       */
      components: {
        PluginAdminLayout,
        PluginSubmissions,
        PluginManagement,
        StatisticsAnalysis,
        CategoryManagement,
        TagManagement,
        DeveloperManagement,
        AuditLog,
        ReviewRules,
        FeedbackManagement,
        PermissionRequestReview,
        PluginTablePermissionManagement
      },
      extensions: {
        // 路由和菜单交由后端 plugin-ui.yml 管理，避免前后端信息重复维护
        routes: [],
        components: [],
        menus: []
      },
      lifecycle: {
        async install(context) {
          const { logger } = context;
          logger.info("Installing AppStoreAdmin plugin");
          const naiveComponents = [
            "NButton",
            "NCard",
            "NInput",
            "NPagination",
            "NSpin",
            "NTag",
            "NModal",
            "NSelect",
            "NTable",
            "NSpace",
            "NAlert",
            "NForm",
            "NFormItem",
            "NInputNumber",
            "NDataTable",
            "NDrawer",
            "NDrawerContent",
            "NEmpty",
            "NTabPane",
            "NTabs",
            "NDescriptions",
            "NDescriptionsItem",
            "NDivider",
            "NList",
            "NListItem",
            "NThing",
            "NPopconfirm",
            "NIcon"
          ];
          if (ui && ui.components && app) {
            naiveComponents.forEach((name) => {
              const component = ui.components[name];
              if (component) {
                app.component(name, component);
              }
            });
            logger.debug("NaiveUI components registered via bridge");
          } else {
            const naiveUI = window.NaiveUI;
            if (naiveUI) {
              const vueApp = window.VueApp;
              if (vueApp) {
                naiveComponents.forEach((name) => {
                  const component = naiveUI[name];
                  if (component) {
                    vueApp.component(name, component);
                  }
                });
                logger.debug("NaiveUI components registered via window (fallback)");
              }
            }
          }
          if (config.debug) {
            logger.debug("AppStoreAdmin plugin installed with config:", config);
          }
        },
        async activate(context) {
          const { logger } = context;
          logger.info("Activating AppStoreAdmin plugin");
        },
        async deactivate(context) {
          const { logger } = context;
          logger.info("Deactivating AppStoreAdmin plugin");
        }
      },
      config: {
        default: config
      },
      extra: {
        category: "application",
        tags: ["admin", "management", "plugin-store"]
      }
    };
  };
  exports.default = index;
  Object.defineProperties(exports, { __esModule: { value: true }, [Symbol.toStringTag]: { value: "Module" } });
  return exports;
}({}, Vue, NaiveUI);
