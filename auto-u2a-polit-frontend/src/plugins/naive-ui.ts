import { App } from 'vue'
import { create, NButton, NInput, NCard, NLayout, NLayoutHeader, NLayoutContent, NLayoutFooter, NLayoutSider, NMenu, NMenuItem, NMenuItemGroup, NSubmenu, NBreadcrumb, NBreadcrumbItem, NPagination, NTable, NTableColumn, NSelect, NOption, NDatePicker, NTimePicker, NCheckbox, NCheckboxGroup, NRadio, NRadioGroup, NRadioButton, NSwitch, NSlider, NRate, NProgress, NAlert, NBadge, NTag, NTooltip, NPopover, NModal, NDrawer, NForm, NFormItem, NInputNumber, NSelectOption, NUpload, NImage, NIcon, NDropdown, NSpace, NDivider, NSpin, NEmpty, NResult, NMessageProvider, NDialogProvider, NLoadingBarProvider } from 'naive-ui'

// 创建Naive UI配置
const naive = create({
  components: [
    NButton,
    NInput,
    NCard,
    NLayout,
    NLayoutHeader,
    NLayoutContent,
    NLayoutFooter,
    NLayoutSider,
    NMenu,
    NMenuItem,
    NMenuItemGroup,
    NSubmenu,
    NBreadcrumb,
    NBreadcrumbItem,
    NPagination,
    NTable,
    NTableColumn,
    NSelect,
    NOption,
    NDatePicker,
    NTimePicker,
    NCheckbox,
    NCheckboxGroup,
    NRadio,
    NRadioGroup,
    NRadioButton,
    NSwitch,
    NSlider,
    NRate,
    NProgress,
    NAlert,
    NBadge,
    NTag,
    NTooltip,
    NPopover,
    NModal,
    NDrawer,
    NForm,
    NFormItem,
    NInputNumber,
    NSelectOption,
    NUpload,
    NImage,
    NIcon,
    NDropdown,
    NSpace,
    NDivider,
    NSpin,
    NEmpty,
    NResult
  ]
})

// 配置Naive UI
export function setupNaiveUI(app: App) {
  app.use(naive)
  app.use(NMessageProvider)
  app.use(NDialogProvider)
  app.use(NLoadingBarProvider)
}