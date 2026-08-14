variable "tenancy_ocid" {
  type = string
}

variable "user_ocid" {
  type = string
}

variable "fingerprint" {
  type      = string
  sensitive = true
}

variable "private_key_path" {
  type      = string
  sensitive = true
}

variable "region" {
  type = string
}

variable "compartment_ocid" {
  type = string
}

variable "vcn_cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "public_subnet_cidr" {
  type    = string
  default = "10.0.1.0/24"
}

variable "ssh_allowed_cidr" {
  description = "CIDR allowed to access the Compute instance through SSH."
  type        = string
}

variable "ssh_public_key" {
  description = "Public SSH key authorized to access the RendaFlex Compute instance."
  type        = string
  sensitive   = true
}

variable "compute_shape" {
  description = "OCI Compute shape used by the RendaFlex application VM."
  type        = string
  default     = "VM.Standard.A1.Flex"
}

variable "compute_ocpus" {
  description = "Number of OCPUs allocated to the RendaFlex VM."
  type        = number
  default     = 2
}

variable "compute_memory_gb" {
  description = "Memory in GB allocated to the RendaFlex VM."
  type        = number
  default     = 12
}
