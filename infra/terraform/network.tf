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
  cidr_block                 = "10.0.2.0/24"
  display_name               = "rendaflex-public-subnet"
  dns_label                  = "public"
  route_table_id             = var.existing_route_table_ocid
  prohibit_public_ip_on_vnic = false
  security_list_ids          = [data.oci_core_vcn.existing.default_security_list_id]
}