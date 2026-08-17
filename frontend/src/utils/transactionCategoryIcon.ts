import {
  Car,
  GraduationCap,
  HeartPulse,
  House,
  Receipt,
  Tag,
  Tv,
  Utensils,
  type LucideIcon,
} from 'lucide-react'
import { TransactionCategory } from '../types'

export const transactionCategoryIcons: Record<TransactionCategory, LucideIcon> = {
  [TransactionCategory.FOOD]: Utensils,
  [TransactionCategory.TRANSPORT]: Car,
  [TransactionCategory.HOUSING]: House,
  [TransactionCategory.HEALTH]: HeartPulse,
  [TransactionCategory.EDUCATION]: GraduationCap,
  [TransactionCategory.ENTERTAINMENT]: Tv,
  [TransactionCategory.SERVICES]: Receipt,
  [TransactionCategory.OTHER]: Tag,
}
