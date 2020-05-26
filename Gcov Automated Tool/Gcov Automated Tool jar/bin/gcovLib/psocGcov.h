#ifndef __PSOC_GCOV_H__
#define __PSOC_GCOV_H__
    
typedef unsigned char   uint8;
typedef unsigned short  uint16;
typedef unsigned long   uint32;
typedef signed   char   int8;
typedef signed   short  int16;
typedef signed   long   int32;
typedef          float  float32;
    
typedef void   (*FUNC_Start)(uint8,uint8);
typedef uint8  (*FUNC_CDCIsReady)();
typedef void   (*FUNC_PutData)(const uint8 *,uint16);
typedef uint8  (*FUNC_IsConfigurationChanged)();
typedef uint8  (*FUNC_GetConfiguration)();
typedef uint8  (*FUNC_CDC_Init)();
typedef uint8  (*FUNC_DataIsReady)();
typedef uint16 (*FUNC_GetAll)(uint8 *);

typedef struct 
{
  FUNC_Start                     Start                    ;
  FUNC_CDCIsReady                CDCIsReady               ;
  FUNC_PutData                   PutData                  ;
  FUNC_IsConfigurationChanged    IsConfigurationChanged   ;
  FUNC_GetConfiguration          GetConfiguration         ;
  FUNC_CDC_Init                  CDC_Init                 ;
  FUNC_DataIsReady               DataIsReady              ;
  FUNC_GetAll                    GetAll                   ;
}USB_DEVICE;
    
#ifdef EXTERN
#undef EXTERN
#endif

#ifdef __PSOC_GCOV_C__
  #define EXTERN  

#else
  /* variable compiled outside embGcov.c */
  #define EXTERN extern
#endif

EXTERN void psocGcov_Init(USB_DEVICE *device);
EXTERN int  psocGcov_Loop();
#endif 

