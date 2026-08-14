data "oci_identity_availability_domains" "available" {
  compartment_id = var.tenancy_ocid
}

data "oci_core_images" "ubuntu" {
  compartment_id   = var.compartment_ocid
  operating_system = "Canonical Ubuntu"
  shape            = var.compute_shape
  sort_by          = "TIMECREATED"
  sort_order       = "DESC"
}

resource "oci_core_instance" "rendaflex" {
  compartment_id      = var.compartment_ocid
  availability_domain = data.oci_identity_availability_domains.available.availability_domains[0].name
  display_name        = "rendaflex-app"
  shape               = var.compute_shape

  shape_config {
    ocpus         = var.compute_ocpus
    memory_in_gbs = var.compute_memory_gb
  }

  create_vnic_details {
    subnet_id        = oci_core_subnet.rendaflex_public.id
    assign_public_ip = true
    hostname_label   = "rendaflex"
    nsg_ids          = [oci_core_network_security_group.rendaflex_app.id]
  }

  source_details {
    source_type = "image"
    source_id   = data.oci_core_images.ubuntu.images[0].id
  }

  metadata = {
    ssh_authorized_keys = var.ssh_public_key
  }
}
