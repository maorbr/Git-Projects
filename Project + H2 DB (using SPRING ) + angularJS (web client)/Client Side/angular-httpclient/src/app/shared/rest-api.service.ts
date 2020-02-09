import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Item } from '../shared/item';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})

export class RestApiService {
  
  apiURL = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }
  
 //RESTful API
 
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  }  

  // GET: [IP Address]:[port]/api/items - Show the List of the inventory items list.
  getItems(): Observable<Item> {
    return this.http.get<Item>(this.apiURL + '/items')
    .pipe(
      retry(1),
      catchError(this.handleError)
    )
  }

  // GET: [IP Address]:[port]/api/items/{id} - Read item details (by item no).
  getItem(id): Observable<Item> {
    return this.http.get<Item>(this.apiURL + '/items/' + id)
    .pipe(
      retry(1),
      catchError(this.handleError)
    )
  }
  
    // PATCH: [IP Address]:[port]/api/items//deposit/{id} - Deposit quantity of a specific item to stock. 
    depositItem(id, item): Observable<Item> {
      return this.http.patch<Item>(this.apiURL + '/items/deposit/' + id, JSON.stringify(item), this.httpOptions)
      .pipe(
        retry(1),
        catchError(this.handleError)
      )
    }
  
      // PATCH: [IP Address]:[port]/api/items/withdrawal/{id} - Withdrawal quantity of a specific item from stock.
      withdrawalItem(id, item): Observable<Item> {
        return this.http.patch<Item>(this.apiURL + '/items/withdrawal/' + id, JSON.stringify(item), this.httpOptions)
        .pipe(
          retry(1),
          catchError(this.handleError)
        )
      }

  // CREATE: Add item to stock.
  createItem(item): Observable<Item> {
    return this.http.post<Item>(this.apiURL + '/items', JSON.stringify(item), this.httpOptions)
    .pipe(
      retry(1),
      catchError(this.handleError)
    )
  }  

  // DELETE: Delete an item from stock.
  deleteItem(id){
    return this.http.delete<Item>(this.apiURL + '/items/' + id, this.httpOptions)
    .pipe(
      retry(1),
      catchError(this.handleError)
    )
  }

  // Error handling 
  handleError(error) {
     let errorMessage = '';
     if(error.error instanceof ErrorEvent) {
       // Get client-side error
       errorMessage = error.error.message;
     } else {
       // Get server-side error
       errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
     }
     window.alert(errorMessage);
     return throwError(errorMessage);
  }

}