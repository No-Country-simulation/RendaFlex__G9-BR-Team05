resource "oci_core_vcn" "rendaflex" {
  compartment_id = var.compartment_ocid
  cidr_block     = var.vcn_cidr
  display_name   = "rendaflex-vcn"
  dns_label      = "rendaflex"
}

resource "oci_core_internet_gateway" "rendaflex" {
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.rendaflex.id
  display_name   = "rendaflex-internet-gateway"
  enabled        = true
}

resource "oci_core_route_table" "rendaflex_public" {
  compartment_id = var.compartment_ocid
  vcn_id         = oci_core_vcn.rendaflex.id
  display_name   = "rendaflex-public-route-table"

  route_rules {
    destination       = "0.0.0.0/0"
    destination_type  = "CIDR_BLOCK"
    network_entity_id = oci_core_internet_gateway.rendaflex.id
  }
}

resource "oci_core_subnet" "rendaflex_public" {
  compartment_id             = var.compartment_ocid
  vcn_id                     = oci_core_vcn.rendaflex.id
  cidr_block                 = var.public_subnet_cidr
  display_name               = "rendaflex-public-subnet"
  dns_label                  = "public"
  route_table_id             = oci_core_route_table.rendaflex_public.id
  prohibit_public_ip_on_vnic = false
}
