resource "oci_core_network_security_group" "rendaflex_app" {
  compartment_id = var.compartment_ocid
  vcn_id         = data.oci_core_vcn.existing.id
  display_name   = "rendaflex-app-nsg"
}

resource "oci_core_network_security_group_security_rule" "rendaflex_ssh_ingress" {
  network_security_group_id = oci_core_network_security_group.rendaflex_app.id
  direction                 = "INGRESS"
  protocol                  = "6"
  source                    = var.ssh_allowed_cidr
  source_type               = "CIDR_BLOCK"
  description               = "Allows SSH administration access to the RendaFlex Compute instance."

  tcp_options {
    destination_port_range {
      min = 22
      max = 22
    }
  }
}

resource "oci_core_network_security_group_security_rule" "rendaflex_api_ingress" {
  network_security_group_id = oci_core_network_security_group.rendaflex_app.id
  direction                 = "INGRESS"
  protocol                  = "6"
  source                    = "0.0.0.0/0"
  source_type               = "CIDR_BLOCK"
  description               = "Exposes the public RendaFlex Spring Boot API."

  tcp_options {
    destination_port_range {
      min = 8080
      max = 8080
    }
  }
}

resource "oci_core_network_security_group_security_rule" "rendaflex_egress" {
  network_security_group_id = oci_core_network_security_group.rendaflex_app.id
  direction                 = "EGRESS"
  protocol                  = "all"
  destination               = "0.0.0.0/0"
  destination_type          = "CIDR_BLOCK"
}