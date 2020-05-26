#include <project.h>
#include "psocGcov.h"

// add these flags to files to be instrumented : 
// -fprofile-arcs -ftest-coverage

int main()
{
  USB_DEVICE usb;            /* create a gcov usb device */  

  /* fill the embUART interface structure */
  usb.CDC_Init               = embUART_CDC_Init;
  usb.CDCIsReady             = embUART_CDCIsReady;
  usb.DataIsReady            = embUART_DataIsReady;
  usb.GetAll                 = embUART_GetAll;
  usb.GetConfiguration       = embUART_GetConfiguration;
  usb.IsConfigurationChanged = embUART_IsConfigurationChanged;
  usb.PutData                = embUART_PutData;
  usb.Start                  = embUART_Start;
    
  psocGcov_Init(&usb);      /* initialize gcov */

  CyGlobalIntEnable;
  for(;;)
  {
    psocGcov_Loop();        /* wait for host communication */
  }
}