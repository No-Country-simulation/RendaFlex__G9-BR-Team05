data "oci_core_vcn" "existing" {
  vcn_id = var.existing_vcn_ocid
}

resource "oci_core_security_list" "rendaflex_empty" {
  compartment_id = var.compartment_ocid
  vcn_id         = data.oci_core_vcn.existing.id
  display_name   = "rendaflex-empty-security-list"
}

resource "oci_core_subnet" "rendaflex_public" {
  compartment_id             = var.compartment_ocid
  vcn_id                     = data.oci_core_vcn.existing.id
  cidr_block                 = var.public_subnet_cidr
  display_name               = "rendaflex-public-subnet"
  dns_label                  = "public"
  route_table_id             = var.existing_route_table_ocid
  prohibit_public_ip_on_vnic = false
  security_list_ids          = [oci_core_security_list.rendaflex_empty.id]
}