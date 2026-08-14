output "vcn_ocid" {
  description = "OCID of the RendaFlex VCN."
  value       = oci_core_vcn.rendaflex.id
}

output "public_subnet_ocid" {
  description = "OCID of the RendaFlex public subnet."
  value       = oci_core_subnet.rendaflex_public.id
}

output "app_nsg_ocid" {
  description = "OCID of the RendaFlex application Network Security Group."
  value       = oci_core_network_security_group.rendaflex_app.id
}

output "compute_instance_ocid" {
  description = "OCID of the RendaFlex Compute instance."
  value       = oci_core_instance.rendaflex.id
}

output "compute_public_ip" {
  description = "Public IP address of the RendaFlex Compute instance."
  value       = oci_core_instance.rendaflex.public_ip
}

output "compute_private_ip" {
  description = "Private IP address of the RendaFlex Compute instance."
  value       = oci_core_instance.rendaflex.private_ip
}
