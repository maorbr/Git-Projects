import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { RestApiService } from "../shared/rest-api.service";

@Component({
  selector: 'app-item-create',
  templateUrl: './item-create.component.html',
  styleUrls: ['./item-create.component.css']
})
export class ItemCreateComponent implements OnInit {

  @Input() itemDetails = { name: '', amount: 0, inventoryCode: 0 }

  constructor(
    public restApi: RestApiService,
    public router: Router
  ) { }

  ngOnInit() {
  }

  addItem(dataItem) {
    this.restApi.createItem(this.itemDetails).subscribe((data: {}) => {
      this.router.navigate(['/items-list'])
    })
  }

}
