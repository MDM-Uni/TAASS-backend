package com.petlife.ospedalemicroservizio.models;

public enum TipoVisita {
   VACCINO, OPERAZIONE, CONTROLLO;
   public String toString() {
      return this.name().toLowerCase();
   }
}
