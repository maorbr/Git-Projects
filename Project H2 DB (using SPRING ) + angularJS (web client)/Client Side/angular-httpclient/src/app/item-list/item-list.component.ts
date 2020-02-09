import { Component, OnInit } from '@angular/core';
import { RestApiService } from "../shared/rest-api.service";

@Component({
  selector: 'app-item-list',
  templateUrl: './item-list.component.html',
  styleUrls: ['./item-list.component.css']
})
export class ItemListComponent implements OnInit {

  Item: any = [];

  constructor(
    public restApi: RestApiService
  ) { }

  ngOnInit() {
    this.loadItems()
  }

  loadItems() {
    return this.restApi.getItems().subscribe((data: {}) => {
      this.Item = data;
    })
  }

  deleteItem(id) {
    if (window.confirm('Are you sure, you want to delete this item?')) {
      this.restApi.deleteItem(id).subscribe(data => {
        this.loadItems()
      })
    }
  }

}
